package com.konai.vam.api.v1.wooribank.cache

import com.konai.vam.core.enumerate.RechargeTransactionType

interface WooriBankAggregationCacheAdapter {

    fun incremantAggregationCache(aggregateDate: String, amount: Long, tranType: RechargeTransactionType): WooriBankAggregationCache

    fun findAggregationCache(aggregateDate: String): WooriBankAggregationCache

    fun deleteAggregationCache(aggregateDate: String)

}