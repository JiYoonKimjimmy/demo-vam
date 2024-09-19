package com.konai.vam.api.v1.wooribank.cache

import com.konai.vam.core.cache.redis.RedisTemplateAdapter
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.enumerate.RechargeTransactionType
import org.springframework.stereotype.Service

@Service
class WooriBankAggregationCacheService(
    private val redisTemplateAdapter: RedisTemplateAdapter,
) : WooriBankAggregationCacheAdapter {

    override fun incremantAggregationCache(aggregateDate: String, amount: Long, tranType: RechargeTransactionType): WooriBankAggregationCache {
        return WooriBankAggregationCache(aggregateDate).increment(redisTemplateAdapter, amount, tranType)
    }

    override fun findAggregationCache(aggregateDate: String): WooriBankAggregationCache {
        return with(WooriBankAggregationCache(aggregateDate)) {
            this.apply(getWooriAggregationCache(this.keys))
        }
    }

    private fun getWooriAggregationCache(keys: List<String>): List<Number> {
        return redisTemplateAdapter.getNumberMultiValues(keys).ifEmpty { throw ResourceNotFoundException(ErrorCode.WOORI_BANK_AGGREGATION_CACHE_NOT_FOUND) }
    }

    override fun deleteAggregationCache(aggregateDate: String) {
        WooriBankAggregationCache(aggregateDate).let { redisTemplateAdapter.delete(it.keys) }
    }
}