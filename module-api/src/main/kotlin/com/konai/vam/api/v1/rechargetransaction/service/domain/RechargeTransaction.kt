package com.konai.vam.api.v1.rechargetransaction.service.domain

import com.konai.vam.api.v1.virtualaccount.service.domain.BankAccount
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.BaseException
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.enumerate.RechargeTransactionCancelStatus
import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.RechargeTransactionType.CANCEL
import com.konai.vam.core.enumerate.RechargeTransactionType.RECHARGE
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.enumerate.Result.FAILED
import com.konai.vam.core.enumerate.Result.SUCCESS
import com.konai.vam.core.restclient.cs.CsPostRechargesSystemManualsResponse
import java.time.LocalDateTime

data class RechargeTransaction(
    val id: Long? = null,
    val tranNo: String,
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
    val cancelOrgTranNo: String? = null,
    val cancelDate: LocalDateTime? = null,
    val errorCode: ErrorCode? = null,
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
        val errorReason = exceptionToErrorReason(exception)
        val convertReason: (Pair<String, String>) -> String = { "[${it.first}] ${it.second}" }
        val convertErrorCode: (Pair<String, String>) -> ErrorCode? = {
            if (this.tranType == RECHARGE) {
                convertRechargeFailedErrorCode(it.first)
            } else {
                ErrorCode.VIRTUAL_ACCOUNT_RECHARGE_CANCEL_FAILED
            }
        }

        return copy(
            result = FAILED,
            reason = convertReason(errorReason),
            errorCode = convertErrorCode(errorReason)
        )
    }

    private fun exceptionToErrorReason(exception: Exception): Pair<String, String> {
        return when (exception) {
            is BaseException -> exception.parseDetailMessage() ?: Pair(exception.errorCode.code, exception.errorCode.message)
            else -> Pair(ErrorCode.UNKNOWN_ERROR.code, ErrorCode.UNKNOWN_ERROR.message)
        }
    }

    private fun convertRechargeFailedErrorCode(errorCode: String): ErrorCode {
        return when (errorCode) {
            // 충전 카드 상태가 ACTIVE 가 아닌 경우
            "24_4000_154", "24_7000_154" -> ErrorCode.RECHARGE_CARD_STATUS_IS_NOT_ACTIVE
            // 충전 대기금 한도 초과인 경우
            "24_3000_334", "24_4000_510" -> ErrorCode.RECHARGE_AMOUNT_EXCEEDED
            // 그외 경우
            else -> ErrorCode.VIRTUAL_ACCOUNT_RECHARGE_FAILED
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