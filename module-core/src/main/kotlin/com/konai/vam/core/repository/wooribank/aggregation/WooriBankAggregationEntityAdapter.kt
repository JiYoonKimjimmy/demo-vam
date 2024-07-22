package com.konai.vam.core.repository.wooribank.aggregation

import com.konai.vam.core.repository.wooribank.aggregation.entity.WooriBankAggregationEntity

interface WooriBankAggregationEntityAdapter {

    fun save(entity: WooriBankAggregationEntity) : WooriBankAggregationEntity

}