package com.konai.vam.api.v1.wooribank.cache

import com.konai.vam.core.cache.redis.RedisTemplateAdapter
import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.RechargeTransactionType.CANCEL
import com.konai.vam.core.enumerate.RechargeTransactionType.RECHARGE

data class WooriBankAggregationCache(
    val aggregateDate: String,
    val konaDepositCount: Int = 0,
    val konaDepositAmount: Long = 0,
    val konaDepositCancelCount: Int = 0,
    val konaDepositCancelAmount: Long = 0,
) {

    val depositCountCacheKey: String by lazy { "vam:woori-bank:aggregate:$aggregateDate:depositCount" }
    val depositAmountCacheKey: String by lazy { "vam:woori-bank:aggregate:$aggregateDate:depositAmount" }
    val depositCancelCountCacheKey: String by lazy { "vam:woori-bank:aggregate:$aggregateDate:depositCancelCount" }
    val depositCancelAmountCacheKey: String by lazy { "vam:woori-bank:aggregate:$aggregateDate:depositCancelAmount" }

    val keys: List<String> by lazy { listOf(depositCountCacheKey, depositAmountCacheKey, depositCancelCountCacheKey, depositCancelAmountCacheKey) }

    fun apply(values: List<Number>): WooriBankAggregationCache {
        return copy(
            konaDepositCount = values[0].toInt(),
            konaDepositAmount = values[1].toLong(),
            konaDepositCancelCount = values[2].toInt(),
            konaDepositCancelAmount = values[3].toLong()
        )
    }

    fun increment(redisTemplateAdapter: RedisTemplateAdapter, amount: Long, tranType: RechargeTransactionType): WooriBankAggregationCache {
        return when (tranType) {
            RECHARGE -> {
                copy(
                    konaDepositCount = redisTemplateAdapter.increment(this.depositCountCacheKey).toInt(),
                    konaDepositAmount = redisTemplateAdapter.increment(this.depositAmountCacheKey, amount)
                )
            }
            CANCEL -> {
                copy(
                    konaDepositCancelCount = redisTemplateAdapter.increment(this.depositCancelCountCacheKey).toInt(),
                    konaDepositCancelAmount = redisTemplateAdapter.increment(this.depositCancelAmountCacheKey, amount)
                )
            }
        }
    }

}