package com.konai.vam.api.v1.rechargetransaction.service.domain

import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.enumerate.ReversalStatus
import java.time.LocalDateTime

data class RechargeTransaction(
    val id: Long? = null,
    val tranNo: String,
    val tranType: RechargeTransactionType,
    val result: Result,
    val reason: String?,
    val amount: Long,
    val bankCode: String,
    val accountNo: String,
    val par: String,
    val serviceId: String,
    val transactionId: String?,
    val nrNumber: String?,
    val rechargerId: String,
    val rechargeDate: LocalDateTime,
    val rechargeCancelDate: LocalDateTime?,
    val reversalStatus: ReversalStatus?,
)