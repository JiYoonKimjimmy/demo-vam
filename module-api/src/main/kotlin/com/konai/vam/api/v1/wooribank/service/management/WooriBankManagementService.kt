package com.konai.vam.api.v1.wooribank.service.management

import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagement
import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagementMapper
import com.konai.vam.api.v1.wooribank.service.transaction.WooriBankTransactionAdapter
import com.konai.vam.api.v1.wooribank.service.transaction.domain.WooriBankTransaction
import com.konai.vam.core.common.error
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.enumerate.WooriBankMessageType.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WooriBankManagementService(

    private val wooriBankManagementMapper: WooriBankManagementMapper,
    private val wooriBankManagementFindAdapter: WooriBankManagementFindAdapter,
    private val wooriBankManagementSaveAdapter: WooriBankManagementSaveAdapter,
    private val wooriBankTransactionAdapter: WooriBankTransactionAdapter

) : WooriBankManagementAdapter {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun management(domain: WooriBankManagement): WooriBankManagement {
        val result = with(beforeProcess(domain)) {
            if (this.responseCode == null) {
                // 우리은행 전문 연동 신규 요청인 경우, 전문 연동 처리
                process(this)
            } else {
                // 우리은행 전문 연동 중복 요청인 경우, 즉시 반환 처리
                this
            }
        }
        return result.convertToResponse()
    }

    private fun beforeProcess(domain: WooriBankManagement): WooriBankManagement {
        // 우리은행 전문 중복 요청 확인 후, 없는 경우 최초 저장
        return checkDuplicatedMessage(domain) ?: saveWooriBankManagement(domain)
    }

    private fun process(domain: WooriBankManagement): WooriBankManagement {
        val result = try {
            executeWooriBankManagement(domain)
        } catch (e: Exception) {
            logger.error(e)
            domain.fail(e)
        }
        return saveWooriBankManagement(result)
    }

    private fun checkDuplicatedMessage(domain: WooriBankManagement): WooriBankManagement? {
        // 우리은행 전문 연동 중복 내역 있는 경우, 완료 처리
        return wooriBankManagementMapper.domainToPredicate(domain)
            .let { wooriBankManagementFindAdapter.findByPredicate(it) }
    }

    private fun saveWooriBankManagement(domain: WooriBankManagement): WooriBankManagement {
        return wooriBankManagementSaveAdapter.save(domain)
    }

    private fun executeWooriBankManagement(domain: WooriBankManagement): WooriBankManagement {
        return when (WooriBankMessageType.find(domain.messageTypeCode, domain.businessTypeCode)) {
            VIRTUAL_ACCOUNT_INQUIRY -> inquiryProc(domain)
            VIRTUAL_ACCOUNT_DEPOSIT -> transactionProc(domain, wooriBankTransactionAdapter::deposit)
            VIRTUAL_ACCOUNT_DEPOSIT_CANCEL -> transactionProc(domain, wooriBankTransactionAdapter::depositCancel)
            VIRTUAL_ACCOUNT_DEPOSIT_CONFIRM -> transactionProc(domain, wooriBankTransactionAdapter::depositConfirm)
            else -> throw InternalServiceException(ErrorCode.WOORI_BANK_MESSAGE_CODE_INVALID)
        }
    }
    private fun inquiryProc(domain: WooriBankManagement): WooriBankManagement {
        return domain.success()
    }

    private fun transactionProc(domain: WooriBankManagement, function: (WooriBankTransaction) -> WooriBankTransaction): WooriBankManagement {
        return wooriBankManagementMapper.domainToTransaction(domain)
            .let(function)
            .let { wooriBankManagementMapper.transactionToDomain(domain, it) }
    }

}