package com.konai.vam.api.v1.wooribank.service.transaction.domain

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.wooribank.service.common.WooriBankErrorResponse
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.enumerate.WooriBankResponseCode
import com.konai.vam.core.enumerate.WooriBankResponseCode.`0000`
import com.konai.vam.core.enumerate.YesOrNo
import com.konai.vam.core.repository.virtualaccountbank.VirtualAccountBankConst

data class WooriBankTransaction(
    var id: Long? = null,
    val identifierCode: String,
    val companyNo : String,
    val institutionCode : String,
    val messageType: WooriBankMessageType,
    val transmissionCount: Int,
    val messageNo: String,
    val transmissionDate: String,
    val transmissionTime: String,
    val responseCode: WooriBankResponseCode?,
    val orgMessageNo: String?,
    val parentAccount: String,
    val trDate: String,
    val trTime: String,
    val tid: String,
    val trMedium: String,
    val trAmount: Long,
    val otherCashierCheckAmount: Long,
    val etcOtherCashierCheckAmount: Long,
    val trBranch: String,
    val depositorName: String?,
    val accountNo: String,
    val cashDepositYn: String?,
    val cashierCheckAmount: Long,
    val branchCode: String?,
    val responseMessage: String? = null,
    val depositConfirm: YesOrNo = YesOrNo.N
) {
    val bankCode = VirtualAccountBankConst.woori.bankCode
    val tranNo: String by lazy { "$trDate$trTime$messageNo" }
    val orgTranNo: String by lazy { "$trDate$trTime$orgMessageNo" }

    lateinit var par: String
    lateinit var serviceId: String
    lateinit var rechargerId: String
    lateinit var transactionId: String

    fun setParAndServiceId(account: VirtualAccount): WooriBankTransaction = apply { this.par = account.par!!; this.serviceId = account.serviceId!! }
    fun setRechargerId(rechargerId: String): WooriBankTransaction = apply { this.rechargerId = rechargerId }
    fun setTransactionId(transactionId: String): WooriBankTransaction = apply { this.transactionId = transactionId }

    fun success(responseCode: WooriBankResponseCode = `0000`): WooriBankTransaction {
        return this.copy(responseCode = responseCode)
    }

    fun fail(exception: Exception): WooriBankTransaction {
        val errorResponse = WooriBankErrorResponse.of(exception)
        return this.copy(
            responseCode = errorResponse.responseCode,
            responseMessage = errorResponse.responseMessage
        )
    }

    fun depositConfirmed(): WooriBankTransaction {
        return this.copy(depositConfirm = YesOrNo.Y)
    }

}