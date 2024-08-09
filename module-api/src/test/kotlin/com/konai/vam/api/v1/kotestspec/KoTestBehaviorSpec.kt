package com.konai.vam.api.v1.kotestspec

import com.konai.vam.api.v1.batch.service.VirtualAccountCardBatchService
import com.konai.vam.api.v1.rechargetransaction.service.RechargeTransactionFindService
import com.konai.vam.api.v1.rechargetransaction.service.RechargeTransactionSaveService
import com.konai.vam.api.v1.rechargetransaction.service.RechargeTransactionService
import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransactionMapper
import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountFindService
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.api.v1.virtualaccountbank.service.VirtualAccountBankFindService
import com.konai.vam.api.v1.virtualaccountbank.service.domain.VirtualAccountBankMapper
import com.konai.vam.api.v1.wooribank.cache.WooriBankAggregationCacheService
import com.konai.vam.api.v1.wooribank.service.aggregation.WooriBankAggregationService
import com.konai.vam.api.v1.wooribank.service.aggregation.domain.WooriBankAggregationMapper
import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagementMapper
import com.konai.vam.api.v1.wooribank.service.transaction.domain.WooriBankTransactionMapper
import com.konai.vam.api.v1.wooribank.service.management.WooriBankManagementFindService
import com.konai.vam.api.v1.wooribank.service.management.WooriBankManagementSaveService
import com.konai.vam.api.v1.wooribank.service.management.WooriBankManagementService
import com.konai.vam.api.v1.wooribank.service.transaction.WooriBankTransactionService
import com.konai.vam.api.v1.wooribank.service.work.WooriBankWorkService
import com.konai.vam.core.cache.redis.RedisTemplateService
import com.konai.vam.core.restclient.cs.CsRestClient
import com.konai.vam.core.restclient.vambatch.VamBatchRestClient
import com.konai.vam.core.restclient.wooribank.WooriBankRestClient
import fixtures.*
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk
import org.springframework.data.redis.core.RedisTemplate

abstract class KoTestBehaviorSpec(body: BaseBehaviorSpec.() -> Unit = {}) : BaseBehaviorSpec() {
    init {
        body()
    }
}

abstract class BaseBehaviorSpec : BehaviorSpec() {

    private val wooriBankTransactionMapper = WooriBankTransactionMapper()

    private val virtualAccountEntityAdaptor = VirtualAccountEntityAdapterFixture()
    private val virtualAccountMapper = VirtualAccountMapper()
    private val virtualAccountFindService = VirtualAccountFindService(virtualAccountEntityAdaptor, virtualAccountMapper)

    private val virtualAccountBankEntityAdaptor = VirtualAccountBankEntityAdapterFixture()
    private val virtualAccountBankMapper = VirtualAccountBankMapper()
    private val virtualAccountBankFindService = VirtualAccountBankFindService(virtualAccountBankEntityAdaptor, virtualAccountBankMapper)

    private val rechargeTransactionEntityAdaptor = RechargeTransactionRepositoryFixture()
    private val rechargeTransactionMapper = RechargeTransactionMapper()
    private val rechargeTransactionSaveService = RechargeTransactionSaveService(rechargeTransactionEntityAdaptor, rechargeTransactionMapper)
    private val rechargeTransactionFindService = RechargeTransactionFindService(rechargeTransactionEntityAdaptor, rechargeTransactionMapper)

    private val mockNumberRedisTemplate = mockk<RedisTemplate<String, Number>>()
    private val redisTemplateService = RedisTemplateService(mockNumberRedisTemplate)
    private val wooriBankAggregationCacheService = WooriBankAggregationCacheService(redisTemplateService)

    private val wooriBankAggregationEntityAdapter = WooriBankAggregationEntityAdapterFixture()
    private val wooriBankAggregationMapper = WooriBankAggregationMapper()
    private val mockWooriBankRestClient = mockk<WooriBankRestClient>()

    private val wooriBankAggregationService = WooriBankAggregationService(wooriBankAggregationCacheService, wooriBankAggregationEntityAdapter, wooriBankAggregationMapper, mockWooriBankRestClient)

    private val mockCsRestClient = mockk<CsRestClient>()

    private val rechargeTransactionService = RechargeTransactionService(rechargeTransactionSaveService, rechargeTransactionFindService, mockCsRestClient)
    private val wooriBankTransactionService = WooriBankTransactionService(wooriBankTransactionMapper, rechargeTransactionService, rechargeTransactionFindService, wooriBankAggregationService, virtualAccountFindService, virtualAccountBankFindService)

    private val wooriBankTransactionFixture = WooriBankTransactionFixture()
    private val rechargeTransactionFixture = RechargeTransactionFixture()
    private val rechargeTransactionEntityFixture = RechargeTransactionEntityFixture()
    private val wooriBankRestClientModelFixture = WooriBankRestClientModelFixture()

    private val wooriBankWorkService = WooriBankWorkService(mockWooriBankRestClient)

    private val wooriBankManagementMapper = WooriBankManagementMapper()
    private val wooriBankManagementEntityAdapter = WooriBankManagementEntityAdapterFixture()
    private val wooriBankManagementFindService = WooriBankManagementFindService(wooriBankManagementMapper, wooriBankManagementEntityAdapter)
    private val wooriBankManagementSaveService = WooriBankManagementSaveService(wooriBankManagementMapper, wooriBankManagementEntityAdapter)
    private val wooriBankManagementService = WooriBankManagementService(wooriBankManagementMapper, wooriBankManagementFindService, wooriBankManagementSaveService, wooriBankTransactionService)
    private val wooriBankManagementFixture = WooriBankManagementFixture()

    private val mockVamBatchRestClient = mockk<VamBatchRestClient>()
    private val virtualAccountCardBatchService = VirtualAccountCardBatchService(mockVamBatchRestClient)

    // service
    fun virtualAccountFindService() = this.virtualAccountFindService
    fun virtualAccountBankFindService() = this.virtualAccountBankFindService
    fun virtualAccountCardBatchService() = this.virtualAccountCardBatchService
    fun wooriBankTransactionService() = this.wooriBankTransactionService
    fun wooriBankAggregationService() = this.wooriBankAggregationService
    fun wooriBankManagementService() = this.wooriBankManagementService
    fun rechargeTransactionService() = this.rechargeTransactionService
    fun rechargeTransactionSaveService() = this.rechargeTransactionSaveService
    fun rechargeTransactionFindService() = this.rechargeTransactionFindService
    fun wooriBankAggregationCacheService() = this.wooriBankAggregationCacheService
    fun wooriBankWorkService() = this.wooriBankWorkService
    fun redisTemplateService() = this.redisTemplateService

    // entity adaptor
    fun virtualAccountEntityAdaptor() = this.virtualAccountEntityAdaptor
    fun virtualAccountBankEntityAdaptor() = this.virtualAccountBankEntityAdaptor
    fun wooriBankAggregationEntityAdapter() = this.wooriBankAggregationEntityAdapter
    fun wooriBankManagementEntityAdapter() = this.wooriBankManagementEntityAdapter

    // repository
    fun rechargeTransactionEntityAdaptor() = this.rechargeTransactionEntityAdaptor

    // mapper
    fun virtualAccountMapper() = this.virtualAccountMapper

    // etc
    fun mockCsRestClient() = this.mockCsRestClient
    fun mockWooriBankRestClient() = this.mockWooriBankRestClient
    fun mockNumberRedisTemplate() = this.mockNumberRedisTemplate
    fun mockVamBatchRestClient() = this.mockVamBatchRestClient

    // fixture
    fun wooriBankTransactionFixture() = this.wooriBankTransactionFixture
    fun rechargeTransactionFixture() = this.rechargeTransactionFixture
    fun rechargeTransactionEntityFixture() = this.rechargeTransactionEntityFixture
    fun wooriBankRestClientModelFixture() = this.wooriBankRestClientModelFixture
    fun wooriBankManagementFixture() = this.wooriBankManagementFixture

}