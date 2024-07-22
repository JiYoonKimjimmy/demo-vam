package fixtures

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.repository.wooribank.aggregation.WooriBankAggregationEntityAdapter
import com.konai.vam.core.repository.wooribank.aggregation.entity.WooriBankAggregationEntity

class WooriBankAggregationEntityAdapterFixture : WooriBankAggregationEntityAdapter {

    private val wooriBankAggregationEntityFixture = WooriBankAggregationEntityFixture()

    fun findByAggregateDate(aggregateDate: String): WooriBankAggregationEntity {
        return wooriBankAggregationEntityFixture.entities
            .find { it.aggregateDate == aggregateDate }
            ?: throw ResourceNotFoundException(ErrorCode.UNKNOWN_ERROR)
    }

    override fun save(entity: WooriBankAggregationEntity): WooriBankAggregationEntity {
        return wooriBankAggregationEntityFixture.save(entity)
    }

}