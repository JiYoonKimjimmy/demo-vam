package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountFindAdapter
import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountSaveAdapter
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.VirtualAccountBatchHistorySaveAdapter
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain.VirtualAccountCardConnect
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain.VirtualAccountCardConnectMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.enumerate.Result.FAILED
import com.konai.vam.core.enumerate.Result.SUCCESS
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.CONNECTED
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.DISCONNECTED
import com.konai.vam.core.enumerate.VirtualAccountConnectType.FIXATION
import com.konai.vam.core.enumerate.VirtualAccountStatus.ACTIVE
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class VirtualAccountCardConnectService(

    private val virtualAccountCardConnectMapper: VirtualAccountCardConnectMapper,
    private val virtualAccountFindAdapter: VirtualAccountFindAdapter,
    private val virtualAccountSaveAdapter: VirtualAccountSaveAdapter,
    private val virtualAccountBatchHistorySaveAdapter: VirtualAccountBatchHistorySaveAdapter

) : VirtualAccountCardConnectAdapter {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun connectCardToVirtualAccounts(domain: VirtualAccountCardConnect): VirtualAccountCardConnect {
        return try {
            // 가상 계좌 연결 카드 정보 검증 처리
            validationCards(domain)
            // 가상 계좌 카드 연결 처리
            connectCardsProc(domain)
            // 가상 계좌 카드 연결 성공 처리
            saveBatchHistory(domain, SUCCESS)
        } catch (e: Exception) {
            logger.error(e.stackTraceToString())
            // 가상 계좌 카드 연결 실페 처리
            saveBatchHistory(domain, FAILED, e)
            throw e
        }
    }

    private fun validationCards(domain: VirtualAccountCardConnect) {
        // 가상 계좌 연결 카드 목록 중 이미 연결된 PAR 정보 있는지 확인
        domain.pars.chunked(1000)
            .any { virtualAccountFindAdapter.existsByPars(it) }
            .takeIf { it }
            ?.let { throw InternalServiceException(ErrorCode.BATCH_ID_ALREADY_CONNECTED) }
    }

    private fun connectCardsProc(domain: VirtualAccountCardConnect) {
        findAllAvailableAccounts(domain.bankCode, domain.pars.size)
            .mapIndexed { i, account -> account.connectedCard(domain.pars[i], domain.serviceId, domain.batchId, CONNECTED) }
            .let { virtualAccountSaveAdapter.saveAll(it) }
    }

    private fun findAllAvailableAccounts(bankCode: String, size: Int): List<VirtualAccount> {
        val predicate = VirtualAccountPredicate(bankCode = bankCode, connectType = FIXATION, status = ACTIVE, cardConnectStatus = DISCONNECTED)
        val pageable = PageableRequest(number = 0, size)
        return virtualAccountFindAdapter
            .findAllByPredicate(predicate, pageable).content.takeIf { it.size == size }
            ?: throw InternalServiceException(ErrorCode.INSUFFICIENT_AVAILABLE_VIRTUAL_ACCOUNTS)
    }

    private fun saveBatchHistory(domain: VirtualAccountCardConnect, result: Result, exception: Exception? = null): VirtualAccountCardConnect {
        return virtualAccountCardConnectMapper.domainToHistory(domain, result, exception)
            .let { virtualAccountBatchHistorySaveAdapter.saveAndFlush(it) }
            .let { domain.setBatchHistory(it) }
    }

}