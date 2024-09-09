package com.konai.vam.api.v1.wooribank.service.management

import com.konai.vam.api.v1.wooribank.service.transaction.WooriBankTransactionAdapter
import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagement
import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagementMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.error
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
        return process(domain).let { this.afterProcess(it) }
    }

    private fun process(domain: WooriBankManagement): WooriBankManagement {
        return try {
            checkDuplicatedMessage(domain) ?: messageProcess(domain)
        } catch (e: Exception) {
            logger.error(e)
            domain.fail(e)
        }
    }

    private fun messageProcess(domain: WooriBankManagement): WooriBankManagement {
        return when (WooriBankMessageType.find(domain.messageTypeCode, domain.businessTypeCode)) {
            VIRTUAL_ACCOUNT_INQUIRY -> inquiryProc(domain)
            VIRTUAL_ACCOUNT_DEPOSIT -> depositProc(domain)
            VIRTUAL_ACCOUNT_DEPOSIT_CANCEL -> depositCancelProc(domain)
            VIRTUAL_ACCOUNT_DEPOSIT_CONFIRM -> depositConfirmProc(domain)
            else -> throw InternalServiceException(ErrorCode.WOORI_BANK_MESSAGE_CODE_INVALID)
        }
    }

    private fun inquiryProc(domain: WooriBankManagement): WooriBankManagement {
        return completed(domain)
    }

    private fun depositProc(domain: WooriBankManagement): WooriBankManagement {
        return wooriBankManagementMapper.domainToTransaction(domain)
            .let { wooriBankTransactionAdapter.deposit(it) }
            .let { wooriBankManagementMapper.transactionToDomain(it) }
    }

    private fun depositCancelProc(domain: WooriBankManagement): WooriBankManagement {
        return wooriBankManagementMapper.domainToTransaction(domain)
            .let { wooriBankTransactionAdapter.depositCancel(it) }
            .let { wooriBankManagementMapper.transactionToDomain(it) }
    }

    private fun depositConfirmProc(domain: WooriBankManagement): WooriBankManagement {
        return wooriBankManagementMapper.domainToTransaction(domain)
            .let { wooriBankTransactionAdapter.depositConfirm(it) }
            .let { wooriBankManagementMapper.transactionToDomain(it) }
    }

    private fun checkDuplicatedMessage(domain: WooriBankManagement): WooriBankManagement? {
        // 우리은행 전문 연동 중복 내역 있는 경우, 완료 처리
        return findDuplicatedMessage(domain)?.let { this.completed(it) }
    }

    private fun findDuplicatedMessage(domain: WooriBankManagement): WooriBankManagement? {
        return wooriBankManagementMapper.domainToPredicate(domain)
            .let { wooriBankManagementFindAdapter.findByPredicate(it) }
    }

    private fun afterProcess(domain: WooriBankManagement): WooriBankManagement {
        return saveWooriBankManagement(domain).let { this.completed(it) }
    }

    private fun saveWooriBankManagement(domain: WooriBankManagement): WooriBankManagement {
        return wooriBankManagementSaveAdapter.save(domain)
    }

    private fun completed(domain: WooriBankManagement): WooriBankManagement {
        val messageCode = runCatching { WooriBankMessageType.find(domain.messageTypeCode, domain.businessTypeCode) }.getOrNull()
        return domain.copy(
            messageTypeCode = messageCode?.responseCode?.messageTypeCode ?: domain.messageTypeCode,
            businessTypeCode = messageCode?.responseCode?.businessTypeCode ?: domain.businessTypeCode
        )
    }

}