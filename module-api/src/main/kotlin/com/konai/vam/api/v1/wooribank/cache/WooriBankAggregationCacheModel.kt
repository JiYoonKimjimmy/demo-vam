package com.konai.vam.api.v1.wooribank.cache

import com.konai.vam.core.cache.redis.RedisTemplateAdaptor
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

    fun incremet(redisTemplateAdaptor: RedisTemplateAdaptor, amount: Long, tranType: RechargeTransactionType): WooriBankAggregationCache {
        return when (tranType) {
            RECHARGE -> {
                copy(
                    konaDepositCount = redisTemplateAdaptor.increment(this.depositCountCacheKey).toInt(),
                    konaDepositAmount = redisTemplateAdaptor.increment(this.depositAmountCacheKey, amount)
                )
            }
            CANCEL -> {
                copy(
                    konaDepositCancelCount = redisTemplateAdaptor.increment(this.depositCancelCountCacheKey).toInt(),
                    konaDepositCancelAmount = redisTemplateAdaptor.increment(this.depositCancelAmountCacheKey, amount)
                )
            }
        }
    }

}