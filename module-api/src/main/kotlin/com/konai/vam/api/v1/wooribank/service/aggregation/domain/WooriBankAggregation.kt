package com.konai.vam.api.v1.wooribank.service.aggregation.domain

import com.konai.vam.api.v1.wooribank.cache.WooriBankAggregationCache
import com.konai.vam.core.common.model.wooribank.WooriBankAggregationMessage
import com.konai.vam.core.enumerate.WooriBankAggregateResult
import com.konai.vam.core.enumerate.WooriBankAggregateResult.*

data class WooriBankAggregation(
    val aggregateDate: String,
    val konaDepositCount: Int = 0,
    val konaDepositAmount: Long = 0,
    val konaDepositCancelCount: Int = 0,
    val konaDepositCancelAmount: Long = 0,
    val konaDepositTrAmount: Long = 0,
    var bankDepositCount: Int? = 0,
    var bankDepositAmount: Long? = 0,
    var bankDepositCancelCount: Int? = 0,
    var bankDepositCancelAmount: Long? = 0,
    var bankDepositTrAmount: Long? = 0,
    var aggregateResult: WooriBankAggregateResult? = null
) {

    constructor(cache: WooriBankAggregationCache): this(
        aggregateDate = cache.aggregateDate,
        konaDepositCount = cache.konaDepositCount,
        konaDepositAmount = cache.konaDepositAmount,
        konaDepositCancelCount = cache.konaDepositCancelCount,
        konaDepositCancelAmount = cache.konaDepositCancelAmount,
        konaDepositTrAmount = cache.konaDepositAmount - cache.konaDepositCancelAmount
    )

    fun changeResultToWaiting(): WooriBankAggregation {
        return apply {
            this.aggregateResult = WAITING
        }
    }

    fun applyBankResult(message: WooriBankAggregationMessage): WooriBankAggregation {
        return apply {
            this.bankDepositCount        = message.bankDepositCount
            this.bankDepositAmount       = message.bankDepositAmount
            this.bankDepositCancelCount  = message.bankDepositCancelCount
            this.bankDepositCancelAmount = message.bankDepositCancelAmount
            this.bankDepositTrAmount     = message.bankDepositTrAmount
        }
    }

    fun changeResultToMatchedOrMismatched(): WooriBankAggregation {
        return apply {
            this.aggregateResult = if (checkBankResult()) MATCHED else { MISMATCHED }
        }
    }

    private fun checkBankResult(): Boolean {
        return this.konaDepositCount == this.bankDepositCount
                && this.konaDepositAmount == this.bankDepositAmount
                && this.konaDepositCancelCount == this.bankDepositCancelCount
                && this.konaDepositCancelAmount == this.bankDepositCancelAmount
                && this.konaDepositTrAmount == this.bankDepositTrAmount
    }

}