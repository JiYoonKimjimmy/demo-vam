package com.konai.vam.core.repository.wooribank.fixture

import com.konai.vam.core.enumerate.WooriBankAggregateResult
import com.konai.vam.core.repository.wooribank.entity.WooriBankAggregationEntity
import java.time.LocalDate

class WooriBankAggregationFixture {

    fun getEntity(aggregateDate: String = LocalDate.now().toString()): WooriBankAggregationEntity {
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

}