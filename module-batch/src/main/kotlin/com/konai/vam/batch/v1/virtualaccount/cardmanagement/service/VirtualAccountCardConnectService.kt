package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountFindAdapter
import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountSaveAdapter
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.VirtualAccountBatchHistorySaveAdapter
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain.VirtualAccountCardConnect
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain.VirtualAccountCardConnectMapper
import com.konai.vam.core.common.error
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.enumerate.Result.FAILED
import com.konai.vam.core.enumerate.Result.SUCCESS
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.CONNECTED
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
            // 가상 계좌 카드 연결 실페 처리
            saveBatchHistory(domain, FAILED, e)
            throw logger.error(e)
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
        findConnectableAccounts(domain.bankCode, domain.pars.size)
            .mapIndexed { index, account -> connectCardToAccount(index, account, domain) }
            .let { virtualAccountSaveAdapter.saveAll(it) }
    }

    private fun findConnectableAccounts(bankCode: String, size: Int): List<VirtualAccount> {
        // 카드 연결 가능한 가상 계좌 목록 전체 조회
        return virtualAccountFindAdapter.findAllConnectableVirtualAccounts(bankCode, size).takeIf { it.size == size }
            // 조회 결과 size 가 다르다면 예외 처리
            ?: throw InternalServiceException(ErrorCode.INSUFFICIENT_AVAILABLE_VIRTUAL_ACCOUNTS)
    }

    private fun connectCardToAccount(index: Int, account: VirtualAccount, domain: VirtualAccountCardConnect): VirtualAccount {
        return account.connectedCard(domain.pars[index], domain.serviceId, domain.batchId, CONNECTED)
    }

    private fun saveBatchHistory(domain: VirtualAccountCardConnect, result: Result, exception: Exception? = null): VirtualAccountCardConnect {
        return virtualAccountCardConnectMapper.domainToHistory(domain, result, exception)
            .let { virtualAccountBatchHistorySaveAdapter.saveAndFlush(it) }
            .let { domain.setBatchHistory(it) }
    }

}