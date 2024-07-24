package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.VirtualAccountBatchHistorySaveAdapter
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.BaseException
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.enumerate.Result.FAILED
import com.konai.vam.core.enumerate.Result.SUCCESS
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.CONNECTED
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.DISCONNECTED
import com.konai.vam.core.enumerate.VirtualAccountConnectType.FIXATION
import com.konai.vam.core.enumerate.VirtualAccountStatus.ACTIVE
import com.konai.vam.core.repository.virtualaccount.VirtualAccountEntityAdapter
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class VirtualAccountCardConnectService(

    private val virtualAccountEntityAdapter: VirtualAccountEntityAdapter,
    private val virtualAccountBatchHistorySaveAdapter: VirtualAccountBatchHistorySaveAdapter,
    private val virtualAccountMapper: VirtualAccountMapper

) : VirtualAccountCardConnectAdapter {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 트랜잭션 처리 요구사항
     * 1. 가상계좌 매핑을 하는 도중에 에러가 발생하면, 전체 롤백
     * 2. 가상계좌 매핑 롤백이 발생하더라도, 가상계좌 배치 정보의 경우엔 FAIL 로 INSERT 해야 한다.
     */
    override fun connectVirtualAccountCard(batchId: String, serviceId: String, bankCode: String, parList: List<String>): VirtualAccountBatchHistory {
        return try {
            // 이미 연결 완료된 카드 여부 확인
            checkCardAlreadyConnected(parList)
            connectCardToVirtualAccount(batchId, serviceId, bankCode, parList)
            saveVirtualAccountBatchHistory(batchId, serviceId, parList, SUCCESS)
        } catch (e: Exception) {
            logger.error(e.stackTraceToString())
            saveVirtualAccountBatchHistory(batchId, serviceId, parList, FAILED, e)
            throw e
        }
    }

    private fun checkCardAlreadyConnected(parList: List<String>): Nothing? {
        return parList
            .chunked(1000)
            .flatMap { virtualAccountEntityAdapter.findAllByPars(it) }
            .takeIf { it.isNotEmpty() }
            ?.let { throw InternalServiceException(ErrorCode.BATCH_ID_ALREADY_CONNECTED) }
    }

    fun connectCardToVirtualAccount(batchId: String, serviceId: String, bankCode: String, pars: List<String>) {
        val predicate = VirtualAccountPredicate(status = ACTIVE, connectType = FIXATION, cardConnectStatus = DISCONNECTED, bankCode = bankCode)
        val entities = virtualAccountEntityAdapter.findAllByPredicate(predicate, pageableRequest = PageableRequest(number = 0, pars.size))
        if (entities.content.isEmpty()) {
            // TODO("카드 상품 목록이 없거나 & `pars.size` 보다 작은 경우, 예외 발생")
            throw InternalServiceException(ErrorCode.INSUFFICIENT_AVAILABLE_VIRTUAL_ACCOUNTS)
        }
        val accounts = virtualAccountMapper.entitiesToDomain(entities).content

        accounts
            .mapIndexed { index, account -> account.connectedCard(pars[index], serviceId, batchId, CONNECTED) }
            .map { virtualAccountMapper.domainToEntity(it) }
            .let { virtualAccountEntityAdapter.saveAll(it) }
    }

    private fun generateVirtualAccountBatchHistory(batchId: String, serviceId: String, parList: List<String>, status: Result, exception: Exception? = null): VirtualAccountBatchHistory {
        val reason = exception?.let {
            when (exception) {
                is BaseException -> exception.errorCode.message
                else -> exception.message ?: ErrorCode.UNKNOWN_ERROR.message
            }
        }

        return VirtualAccountBatchHistory(
            cardSeBatchId = batchId,
            serviceId = serviceId,
            count = parList.size,
            result = status,
            reason = reason
        )
    }

    private fun saveVirtualAccountBatchHistory(batchId: String, serviceId: String, parList: List<String>, status: Result, exception: Exception? = null): VirtualAccountBatchHistory {
        return generateVirtualAccountBatchHistory(batchId, serviceId, parList, status, exception)
            .let { virtualAccountBatchHistorySaveAdapter.saveAndFlush(it) }
    }

}

