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

data class WooriBankManagement(
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
    val parentAccount: String,
    val trDate: String,
    val trTime: String,
    val tid: String,
    val trMedium: String,
    val trAmount: Int,
    val otherCashierCheckAmount: Int,
    val etcOtherCashierCheckAmount: Int,
    val trBranch: String,
    val depositorName: String,
    val accountNo: String,
    val accountName: String = WOORI_BANK_ACCOUNT_NAME,
    val accountBalance: Long = WOORI_BANK_ACCOUNT_BALANCE,
    val cashDepositYn: String,
    val cashierCheckAmount: Int,
    val branchCode: String,
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

    fun fail(exception: Exception): WooriBankManagement {
        val errorResponse = WooriBankErrorResponse.of(exception)
        return this.copy(
            responseCode = errorResponse.responseCode,
            responseMessage = errorResponse.responseMessage
        )
    }

}