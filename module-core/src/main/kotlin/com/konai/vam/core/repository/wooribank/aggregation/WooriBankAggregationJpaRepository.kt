package com.konai.vam.core.repository.wooribank.aggregation

import com.konai.vam.core.repository.wooribank.aggregation.entity.WooriBankAggregationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WooriBankAggregationJpaRepository : JpaRepository<WooriBankAggregationEntity, Long> {
    fun findByAggregateDate(aggregateDate: String): WooriBankAggregationEntity
}