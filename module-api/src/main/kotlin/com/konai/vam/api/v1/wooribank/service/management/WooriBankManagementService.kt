package com.konai.vam.api.v1.wooribank.service.management

import com.konai.vam.api.v1.wooribank.service.transaction.WooriBankTransactionAdapter
import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagement
import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagementMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.error
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.enumerate.WooriBankMessageType.*
import com.konai.vam.core.enumerate.WooriBankResponseCode
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
        val result = with(domain.beforeProcess()) {
            if (this.responseCode == null) {
                // 우리은행 전문 연동 신규 요청인 경우, 전문 연동 처리
                this.process()
            } else {
                // 우리은행 전문 연동 중복 요청인 경우, 즉시 반환 처리
                this
            }
        }
        return result.convertToResponseCode()
    }

    private fun WooriBankManagement.beforeProcess(): WooriBankManagement {
        // 우리은행 전문 중복 요청 확인 후, 없는 경우 최초 저장
        return checkDuplicatedMessage(this) ?: saveWooriBankManagement(this)
    }

    private fun WooriBankManagement.process(): WooriBankManagement {
        val result =  try {
            executeMessageProcess(this)
        } catch (e: Exception) {
            logger.error(e)
            this.fail(e)
        }
        return saveWooriBankManagement(result)
    }

    private fun checkDuplicatedMessage(domain: WooriBankManagement): WooriBankManagement? {
        // 우리은행 전문 연동 중복 내역 있는 경우, 완료 처리
        return wooriBankManagementMapper.domainToPredicate(domain)
            .let { wooriBankManagementFindAdapter.findByPredicate(it) }
            ?.takeIf { it.responseCode == WooriBankResponseCode.`0000` }
    }

    private fun saveWooriBankManagement(domain: WooriBankManagement): WooriBankManagement {
        return wooriBankManagementSaveAdapter.save(domain)
    }

    private fun executeMessageProcess(domain: WooriBankManagement): WooriBankManagement {
        return when (WooriBankMessageType.find(domain.messageTypeCode, domain.businessTypeCode)) {
            VIRTUAL_ACCOUNT_INQUIRY -> inquiryProc(domain)
            VIRTUAL_ACCOUNT_DEPOSIT -> depositProc(domain)
            VIRTUAL_ACCOUNT_DEPOSIT_CANCEL -> depositCancelProc(domain)
            VIRTUAL_ACCOUNT_DEPOSIT_CONFIRM -> depositConfirmProc(domain)
            else -> throw InternalServiceException(ErrorCode.WOORI_BANK_MESSAGE_CODE_INVALID)
        }
    }

    private fun inquiryProc(domain: WooriBankManagement): WooriBankManagement {
        return domain.success()
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

}