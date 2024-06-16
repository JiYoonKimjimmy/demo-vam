package com.konai.vam.core.repository.rechargetransaction.entity

import com.konai.vam.core.common.converter.EncryptionCardInfoConverter
import com.konai.vam.core.common.entity.BaseEntity
import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.enumerate.ReversalStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "RECHARGE_TRANSACTIONS")
class RechargeTransactionEntity(

    @Id
    @GeneratedValue
    val id: Long? = null,
    val tranNo: String,
    @Enumerated(EnumType.STRING)
    val tranType: RechargeTransactionType,
    @Enumerated(EnumType.STRING)
    val result: Result,
    val reason: String?,
    val amount: Long,
    val bankCode: String,
    @Convert(converter = EncryptionCardInfoConverter::class)
    val accountNo: String,
    val par: String,
    val serviceId: String,
    val transactionId: String?,
    val nrNumber: String?,
    val rechargerId: String,
    val rechargeDate: LocalDateTime,
    val rechargeCancelDate: LocalDateTime?,
    @Enumerated(EnumType.STRING)
    val reversalStatus: ReversalStatus?,

) : BaseEntity()