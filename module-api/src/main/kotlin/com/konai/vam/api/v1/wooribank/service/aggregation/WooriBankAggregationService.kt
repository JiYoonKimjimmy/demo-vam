package com.konai.vam.api.v1.wooribank.service.aggregation

import com.konai.vam.api.v1.wooribank.cache.WooriBankAggregationCacheAdapter
import com.konai.vam.api.v1.wooribank.service.aggregation.domain.WooriBankAggregation
import com.konai.vam.api.v1.wooribank.service.aggregation.domain.WooriBankAggregationMapper
import com.konai.vam.api.v1.wooribank.service.common.WooriBankCommonMessageAdapter
import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.WooriBankAggregateResult.MATCHED
import com.konai.vam.core.enumerate.WooriBankMessage
import com.konai.vam.core.repository.wooribank.aggregation.WooriBankAggregationEntityAdapter
import com.konai.vam.core.restclient.wooribank.WooriBankRestClient
import org.springframework.stereotype.Service

@Service
class WooriBankAggregationService(

    private val wooriBankAggregationCacheAdapter: WooriBankAggregationCacheAdapter,
    private val wooriBankAggregationEntityAdapter: WooriBankAggregationEntityAdapter,
    private val wooriBankCommonMessageAdapter: WooriBankCommonMessageAdapter,

    private val wooriBankAggregationMapper: WooriBankAggregationMapper,
    private val wooriBankRestClient: WooriBankRestClient

) : WooriBankAggregationAdapter {

    override fun aggregateTransaction(aggregateDate: String): WooriBankAggregation {
        return with(findWooriBankAggregation(aggregateDate)) {
            changeResultToWaiting(this)
            applyBankAggregationResult(this)
            changeResultToMatchedOrMismatched(this)
            delteWooriBankAggregation(this)
        }
    }

    private fun findWooriBankAggregation(aggregateDate: String): WooriBankAggregation {
        return WooriBankAggregation(cache = wooriBankAggregationCacheAdapter.findAggregationCache(aggregateDate))
    }

    private fun changeResultToWaiting(domain: WooriBankAggregation): WooriBankAggregation {
        return save(domain.changeResultToWaiting())
    }

    private fun applyBankAggregationResult(domain: WooriBankAggregation): WooriBankAggregation {
        return wooriBankCommonMessageAdapter.generateCommonMessage(WooriBankMessage.TRANSACTION_AGGREGATION.requestCode)
            .let { wooriBankAggregationMapper.domainToClientRequest(domain, it) }
            .let { wooriBankRestClient.postWooriAggregateTransaction(it) }
            .let { domain.applyBankResult(it) }
    }

    private fun changeResultToMatchedOrMismatched(domain: WooriBankAggregation): WooriBankAggregation {
        return save(domain.changeResultToMatchedOrMismatched())
    }

    private fun save(domain: WooriBankAggregation): WooriBankAggregation {
        return wooriBankAggregationMapper.domainToEntity(domain)
            .let { wooriBankAggregationEntityAdapter.save(it) }
            .let { wooriBankAggregationMapper.entityToDomain(it) }
    }

    private fun delteWooriBankAggregation(domain: WooriBankAggregation): WooriBankAggregation {
        if (domain.aggregateResult == MATCHED) {
            wooriBankAggregationCacheAdapter.deleteAggregationCache(domain.aggregateDate)
        }
        return domain
    }

    override fun incrementAggregation(aggregateDate: String, amount: Long, tranType: RechargeTransactionType): WooriBankAggregation {
        return WooriBankAggregation(cache =  wooriBankAggregationCacheAdapter.incremantAggregationCache(aggregateDate, amount, tranType))
    }

}