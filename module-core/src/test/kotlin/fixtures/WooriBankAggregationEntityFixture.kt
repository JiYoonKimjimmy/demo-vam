package fixtures

import com.konai.vam.core.enumerate.WooriBankAggregateResult
import com.konai.vam.core.repository.wooribank.aggregation.entity.WooriBankAggregationEntity

class WooriBankAggregationEntityFixture {

    val entities = mutableListOf<WooriBankAggregationEntity>()

    fun make(aggregateDate: String): WooriBankAggregationEntity {
        return WooriBankAggregationEntity(
            aggregateDate = aggregateDate,
            konaDepositCount = 1000,
            konaDepositAmount = 10000,
            konaDepositCancelCount = 0,
            konaDepositCancelAmount = 0,
            konaDepositTrAmount = 10000,
            bankDepositCount = 1000,
            bankDepositAmount = 10000,
            bankDepositCancelCount = 0,
            bankDepositCancelAmount = 0,
            bankDepositTrAmount = 10000,
            aggregateResult = WooriBankAggregateResult.MATCHED,
        )
    }

    fun save(entity: WooriBankAggregationEntity): WooriBankAggregationEntity {
        deleteDuplicated(entity.aggregateDate)
        entities += entity
        return entity
    }

    private fun deleteDuplicated(aggregateDate: String) {
        entities.removeIf { it.aggregateDate == aggregateDate }
    }

}