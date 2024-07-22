package com.konai.vam.core.repository.wooribank.aggregation

import com.konai.vam.core.repository.wooribank.aggregation.entity.WooriBankAggregationEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Repository
class WooriBankAggregationRepository(
    private val wooriBankAggregationJpaRepository: WooriBankAggregationJpaRepository
) : WooriBankAggregationEntityAdapter {

    @Transactional
    override fun save(entity: WooriBankAggregationEntity) : WooriBankAggregationEntity {
        return wooriBankAggregationJpaRepository.save(entity)
    }

}