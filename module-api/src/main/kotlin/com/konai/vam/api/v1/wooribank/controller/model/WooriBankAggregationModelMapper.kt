package com.konai.vam.api.v1.wooribank.controller.model

import com.konai.vam.api.v1.wooribank.service.aggregation.domain.WooriBankAggregation
import org.springframework.stereotype.Component

@Component
class WooriBankAggregationModelMapper {

    fun domainToModel(domain: WooriBankAggregation): WooriBankAggregationModel {
        return WooriBankAggregationModel(
            aggregateDate = domain.aggregateDate,
            konaDepositCount = domain.konaDepositCount,
            konaDepositAmount = domain.konaDepositAmount,
            konaDepositCancelCount = domain.konaDepositCancelCount,
            konaDepositCancelAmount = domain.konaDepositCancelAmount,
            konaDepositTrAmount = domain.konaDepositTrAmount,
            bankDepositCount = domain.bankDepositCount,
            bankDepositAmount = domain.bankDepositAmount,
            bankDepositCancelCount = domain.bankDepositCancelCount,
            bankDepositCancelAmount = domain.bankDepositCancelAmount
        )
    }

}