package com.konai.vam.core.repository.wooribank.entity

import com.konai.vam.core.common.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "WOORI_BANK_TRANSACTIONS")
class WooriBankTransactionEntity(

    @Id
    @GeneratedValue
    val id: Long? = null,
    val messageNo: String,
    val orgMessageNo: String?,
    val messageTypeCode: String,
    val businessTypeCode: String,
    val trDate: String,
    val trTime: String,
    val tid: String,
    val trMedium: String,
    val trAmount: String,
    val selfDrawnBillAmount: String,
    val etcDrawnBillAmount: String,
    val trBranch: String,
    val depositorName: String,
    val accountNo: String,
    val cashReceiptYn: String,
    val selfDrawnCheck: String,
    val depositBranchCode: String,
    val responseCode: String,
    val depositConfirmation: String?,

) : BaseEntity()