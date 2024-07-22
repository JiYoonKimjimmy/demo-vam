package fixtures

import com.konai.vam.api.v1.wooribank.service.aggregation.domain.WooriBankAggregation
import com.konai.vam.core.enumerate.WooriBankAggregateResult
import java.time.LocalDate

class WooriBankAggregationFixture {

    fun getOne(aggregateDate: String = LocalDate.now().toString()): WooriBankAggregation {
        return WooriBankAggregation(
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

}