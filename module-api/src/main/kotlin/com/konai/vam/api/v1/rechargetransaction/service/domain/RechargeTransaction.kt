package com.konai.vam.api.v1.rechargetransaction.service.domain

import com.konai.vam.api.v1.virtualaccount.service.domain.BankAccount
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.BaseException
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.enumerate.RechargeTransactionCancelStatus
import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.RechargeTransactionType.CANCEL
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.enumerate.Result.FAILED
import com.konai.vam.core.enumerate.Result.SUCCESS
import com.konai.vam.core.restclient.cs.CsPostRechargesSystemManualsResponse
import java.time.LocalDateTime

data class RechargeTransaction(
    val id: Long? = null,
    val tranNo: String,
    val orgTranNo: String = tranNo,
    val tranType: RechargeTransactionType,
    var result: Result? = null,
    var reason: String? = null,
    val amount: Long,
    val bankAccount: BankAccount,
    val par: String,
    val serviceId: String,
    var transactionId: String? = null,
    var nrNumber: String? = null,
    val rechargerId: String? = null,
    val rechargeDate: LocalDateTime? = null,
    val cancelStatus: RechargeTransactionCancelStatus? = null,
    val cancelDate: LocalDateTime? = null,
) {

    fun successRecharge(response: CsPostRechargesSystemManualsResponse): RechargeTransaction {
        return copy(
            result = SUCCESS,
            transactionId = response.transactionId,
            nrNumber = response.nrNumber,
            rechargeDate = LocalDateTime.now(),
        )
    }

    fun successCancel(origin: RechargeTransaction): RechargeTransaction {
        return copy(
            tranType = CANCEL,
            result = SUCCESS,
            transactionId = origin.transactionId,
            cancelDate = LocalDateTime.now(),
        )
    }

    fun fail(exception: Exception): RechargeTransaction {
        return copy(
            result = FAILED,
            reason = exceptionMessage(exception),
        )
    }

    private fun exceptionMessage(exception: Exception): String {
        return when (exception) {
            is BaseException -> exception.parseDetailMessage() ?: "[${exception.errorCode.code}] ${exception.errorCode.message}"
            else -> "[${ErrorCode.UNKNOWN_ERROR.code}] ${ErrorCode.UNKNOWN_ERROR.message}"
        }
    }

    fun checkIfDepositCanBeCanceled(): RechargeTransaction {
        if (this.cancelStatus == RechargeTransactionCancelStatus.CANCEL) throw InternalServiceException(ErrorCode.RECHARGE_TRANSACTION_ALREADY_CANCELED)
        if (this.transactionId == null) throw InternalServiceException(ErrorCode.RECHARGE_TRANSACTION_IS_INVALID)
        return this
    }

    fun checkIfDepositCanBeConfirmed(): RechargeTransaction {
        if (this.cancelStatus == RechargeTransactionCancelStatus.CANCEL) throw InternalServiceException(ErrorCode.RECHARGE_TRANSACTION_ALREADY_CANCELED)
        return this
    }

    fun canceled(): RechargeTransaction {
        return copy(
            cancelStatus = RechargeTransactionCancelStatus.CANCEL,
            cancelDate = LocalDateTime.now(),
        )
    }

}