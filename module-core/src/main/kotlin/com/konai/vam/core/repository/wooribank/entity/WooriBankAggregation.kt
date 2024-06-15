package com.konai.vam.core.repository.wooribank.entity

import com.konai.vam.core.enumerate.WooriBankAggregateResult
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id

@Entity(name = "WOORI_BANK_AGGREGATIONS")
class WooriBankAggregationEntity(
    @Id
    val aggregateDate: String,
    val konaDepositCount: Int,
    val konaDepositAmount: Long,
    val konaDepositCancelCount: Int,
    val konaDepositCancelAmount: Long,
    val konaDepositTrAmount: Long,
    val bankDepositCount: Int?,
    val bankDepositAmount: Long?,
    val bankDepositCancelCount: Int?,
    val bankDepositCancelAmount: Long?,
    val bankDepositTrAmount: Long?,
    @Enumerated(EnumType.STRING)
    val aggregateResult: WooriBankAggregateResult?
)