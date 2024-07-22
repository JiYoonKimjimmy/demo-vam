package com.konai.vam.api.v1.wooribank.service.aggregation

import com.konai.vam.api.v1.wooribank.service.aggregation.domain.WooriBankAggregation
import com.konai.vam.core.enumerate.RechargeTransactionType

interface WooriBankAggregationAdapter {

    fun aggregateTransaction(aggregateDate: String): WooriBankAggregation

    fun incrementAggregation(aggregateDate: String, amount: Long, tranType: RechargeTransactionType): WooriBankAggregation

}