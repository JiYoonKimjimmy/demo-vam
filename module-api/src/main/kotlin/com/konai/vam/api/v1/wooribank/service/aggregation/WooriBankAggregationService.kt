package com.konai.vam.api.v1.wooribank.service.aggregation

import com.konai.vam.api.v1.wooribank.cache.WooriBankAggregationCacheAdapter
import com.konai.vam.api.v1.wooribank.service.aggregation.domain.WooriBankAggregation
import com.konai.vam.api.v1.wooribank.service.aggregation.domain.WooriBankAggregationMapper
import com.konai.vam.api.v1.wooribank.service.message.WooriBankMessageGenerateAdapter
import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.WooriBankAggregateResult.MATCHED
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.repository.wooribank.aggregation.WooriBankAggregationEntityAdapter
import com.konai.vam.core.restclient.wooribank.WooriBankRestClient
import org.springframework.stereotype.Service

@Service
class WooriBankAggregationService(

    private val wooriBankAggregationCacheAdapter: WooriBankAggregationCacheAdapter,
    private val wooriBankAggregationEntityAdapter: WooriBankAggregationEntityAdapter,
    private val wooriBankMessageGenerateAdapter: WooriBankMessageGenerateAdapter,

    private val wooriBankAggregationMapper: WooriBankAggregationMapper,
    private val wooriBankRestClient: WooriBankRestClient

) : WooriBankAggregationAdapter {

    override fun aggregateTransaction(aggregateDate: String): WooriBankAggregation {
        // 우리은행 집계 Cache 정보 조회
        return with(findWooriBankAggregation(aggregateDate)) {
            // 우리은행 집계 DB 정보 'WAITING' 상태 저장
            changeResultToWaiting(this)
            // 우리은행 집계 결과 조회
            inquiryBankAggregationResult(this)
            // 우리은행 집계 결과 DB 정보 저장
            changeResultToMatchedOrMismatched(this)
            // 우리은행 집계 Cache 정보 삭제
            deleteWooriBankAggregation(this)
        }
    }

    private fun findWooriBankAggregation(aggregateDate: String): WooriBankAggregation {
        return WooriBankAggregation(cache = wooriBankAggregationCacheAdapter.findAggregationCache(aggregateDate))
    }

    private fun changeResultToWaiting(domain: WooriBankAggregation): WooriBankAggregation {
        return save(domain.changeResultToWaiting())
    }

    private fun inquiryBankAggregationResult(domain: WooriBankAggregation): WooriBankAggregation {
        return wooriBankMessageGenerateAdapter.generateMessage(WooriBankMessageType.TRANSACTION_AGGREGATION.requestCode)
            .let { wooriBankAggregationMapper.domainToMessage(domain, it) }
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

    private fun deleteWooriBankAggregation(domain: WooriBankAggregation): WooriBankAggregation {
        if (domain.aggregateResult == MATCHED) {
            wooriBankAggregationCacheAdapter.deleteAggregationCache(domain.aggregateDate)
        }
        return domain
    }

    override fun incrementAggregation(aggregateDate: String, amount: Long, tranType: RechargeTransactionType): WooriBankAggregation {
        return WooriBankAggregation(cache =  wooriBankAggregationCacheAdapter.incremantAggregationCache(aggregateDate, amount, tranType))
    }

}