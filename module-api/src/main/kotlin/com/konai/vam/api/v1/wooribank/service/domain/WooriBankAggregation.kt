package com.konai.vam.api.v1.wooribank.service.domain

import com.konai.vam.core.enumerate.WooriBankAggregateResult

data class WooriBankAggregation(
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
    val aggregateResult: WooriBankAggregateResult?
)