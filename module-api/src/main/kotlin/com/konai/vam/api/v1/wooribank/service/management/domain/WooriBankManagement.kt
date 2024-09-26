package com.konai.vam.api.v1.wooribank.service.management.domain

import com.konai.vam.api.v1.wooribank.service.common.WooriBankErrorResponse
import com.konai.vam.core.common.WOORI_BANK_ACCOUNT_BALANCE
import com.konai.vam.core.common.WOORI_BANK_ACCOUNT_NAME
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.enumerate.WooriBankMessageType.*
import com.konai.vam.core.enumerate.WooriBankResponseCode
import com.konai.vam.core.enumerate.YesOrNo
import com.konai.vam.core.util.convertPatternOf
import java.time.LocalTime

data class WooriBankManagement(
    val id: Long? = null,
    val identifierCode: String,
    val companyNo : String,
    val institutionCode : String,
    val messageTypeCode: String,
    val businessTypeCode: String,
    val transmissionCount: Int,
    val messageNo: String,
    val transmissionDate: String,
    val transmissionTime: String,
    var responseCode: WooriBankResponseCode?,
    val orgMessageNo: String?,
    val parentAccountNo: String,
    val trDate: String,
    val trTime: String,
    val tid: String,
    val trMedium: String,
    val trAmount: Int,
    val otherCashierCheckAmount: Int,
    val etcOtherCashierCheckAmount: Int,
    val trBranch: String,
    val depositorName: String?,
    val accountNo: String,
    val accountName: String = WOORI_BANK_ACCOUNT_NAME,
    val accountBalance: Long = WOORI_BANK_ACCOUNT_BALANCE,
    val cashDepositYn: String?,
    val cashierCheckAmount: Int,
    val branchCode: String?,
    val depositConfirm: YesOrNo = YesOrNo.N,
    val responseMessage: String? = null
) {

    fun checkMessageTypeCode(): WooriBankManagement {
        return when (WooriBankMessageType.find(this.messageTypeCode, this.businessTypeCode)) {
            VIRTUAL_ACCOUNT_INQUIRY -> this
            VIRTUAL_ACCOUNT_DEPOSIT -> this
            VIRTUAL_ACCOUNT_DEPOSIT_CANCEL -> this
            VIRTUAL_ACCOUNT_DEPOSIT_CONFIRM -> this
            else -> throw InternalServiceException(ErrorCode.WOORI_BANK_MESSAGE_CODE_INVALID)
        }
    }

    fun success(): WooriBankManagement {
        return this.copy(responseCode = WooriBankResponseCode.`0000`)
    }

    fun fail(exception: Exception): WooriBankManagement {
        val errorResponse = WooriBankErrorResponse.of(exception)
        return this.copy(
            responseCode = errorResponse.responseCode,
            responseMessage = errorResponse.responseMessage
        )
    }

    fun convertToResponse(): WooriBankManagement {
        val messageType = runCatching { WooriBankMessageType.find(this.messageTypeCode, this.businessTypeCode) }.getOrNull()
        return this.copy(
            transmissionTime = LocalTime.now().convertPatternOf(),
            messageTypeCode = messageType?.responseCode?.messageTypeCode ?: this.messageTypeCode,
            businessTypeCode = messageType?.responseCode?.businessTypeCode ?: this.businessTypeCode,
            responseCode = messageType?.let(this::convertResponseCode) ?: this.responseCode
        )
    }

    private fun convertResponseCode(messageType: WooriBankMessageType): WooriBankResponseCode? {
        return if (messageType == VIRTUAL_ACCOUNT_DEPOSIT_CANCEL && this.messageNo == this.orgMessageNo) {
            // 우리은행 '자동 취소' 전문 요청인 경우, 응답코드 `0000` 변환하여 응답 처리
            WooriBankResponseCode.`0000`
        } else {
            this.responseCode
        }
    }

}