package com.konai.vam.api.v1.wooribank.service.aggregation.domain

import com.konai.vam.core.repository.wooribank.aggregation.entity.WooriBankAggregationEntity
import com.konai.vam.core.restclient.wooribank.PostWooriAggregateTransactionModel
import com.konai.vam.core.restclient.wooribank.PostWooriAggregateTransactionRequest
import org.springframework.stereotype.Component

@Component
class WooriBankAggregationMapper {

    fun domainToEntity(domain: WooriBankAggregation): WooriBankAggregationEntity {
        return WooriBankAggregationEntity(
            aggregateDate = domain.aggregateDate,
            konaDepositCount = domain.konaDepositCount,
            konaDepositAmount = domain.konaDepositAmount,
            konaDepositCancelCount = domain.konaDepositCancelCount,
            konaDepositCancelAmount = domain.konaDepositCancelAmount,
            konaDepositTrAmount = domain.konaDepositTrAmount,
            bankDepositCount = domain.bankDepositCount,
            bankDepositAmount = domain.bankDepositAmount,
            bankDepositCancelCount = domain.bankDepositCancelCount,
            bankDepositCancelAmount = domain.bankDepositCancelAmount,
            bankDepositTrAmount = domain.bankDepositTrAmount,
            aggregateResult = domain.aggregateResult
        )
    }

    fun entityToDomain(entity: WooriBankAggregationEntity): WooriBankAggregation {
        return WooriBankAggregation(
            aggregateDate = entity.aggregateDate,
            konaDepositCount = entity.konaDepositCount,
            konaDepositAmount = entity.konaDepositAmount,
            konaDepositCancelCount = entity.konaDepositCancelCount,
            konaDepositCancelAmount = entity.konaDepositCancelAmount,
            konaDepositTrAmount = entity.konaDepositTrAmount,
            bankDepositCount = entity.bankDepositCount,
            bankDepositAmount = entity.bankDepositAmount,
            bankDepositCancelCount = entity.bankDepositCancelCount,
            bankDepositCancelAmount = entity.bankDepositCancelAmount,
            bankDepositTrAmount = entity.bankDepositTrAmount,
            aggregateResult = entity.aggregateResult
        )
    }

    fun domainToClientRequest(domain: WooriBankAggregation): PostWooriAggregateTransactionRequest {
        return PostWooriAggregateTransactionRequest(
            PostWooriAggregateTransactionModel(
                aggregationDate = domain.aggregateDate,
                konaDepositCount = domain.konaDepositCount,
                konaDepositAmount = domain.konaDepositAmount,
                konaDepositCancelCount = domain.konaDepositCancelCount,
                konaDepositCancelAmount = domain.konaDepositCancelAmount,
                konaDepositTrAmount = domain.konaDepositTrAmount
            )
        )
    }

}