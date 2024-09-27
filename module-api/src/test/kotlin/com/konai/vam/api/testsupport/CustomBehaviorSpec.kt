package com.konai.vam.api.testsupport

import com.konai.vam.api.v1.batch.service.VirtualAccountCardBatchService
import com.konai.vam.api.v1.parentaccount.service.ParentAccountDeleteService
import com.konai.vam.api.v1.parentaccount.service.ParentAccountFindService
import com.konai.vam.api.v1.parentaccount.service.ParentAccountManagementService
import com.konai.vam.api.v1.parentaccount.service.ParentAccountSaveService
import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccountMapper
import com.konai.vam.api.v1.rechargetransaction.service.RechargeTransactionFindService
import com.konai.vam.api.v1.rechargetransaction.service.RechargeTransactionSaveService
import com.konai.vam.api.v1.rechargetransaction.service.RechargeTransactionService
import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransactionMapper
import com.konai.vam.api.v1.sequencegenerator.service.SequenceGeneratorService
import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountFindService
import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountWriteService
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.api.v1.virtualaccountbank.service.VirtualAccountBankFindService
import com.konai.vam.api.v1.virtualaccountbank.service.domain.VirtualAccountBankMapper
import com.konai.vam.api.v1.wooribank.cache.WooriBankAggregationCacheService
import com.konai.vam.api.v1.wooribank.service.aggregation.WooriBankAggregationService
import com.konai.vam.api.v1.wooribank.service.aggregation.domain.WooriBankAggregationMapper
import com.konai.vam.api.v1.wooribank.service.management.WooriBankManagementFindService
import com.konai.vam.api.v1.wooribank.service.management.WooriBankManagementSaveService
import com.konai.vam.api.v1.wooribank.service.management.WooriBankManagementService
import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagementMapper
import com.konai.vam.api.v1.wooribank.service.message.WooriBankMessageGenerateService
import com.konai.vam.api.v1.wooribank.service.transaction.WooriBankTransactionService
import com.konai.vam.api.v1.wooribank.service.transaction.domain.WooriBankTransactionMapper
import com.konai.vam.api.v1.wooribank.service.work.WooriBankWorkService
import com.konai.vam.core.cache.redis.RedisTemplateService
import com.konai.vam.core.restclient.cs.CsRestClient
import com.konai.vam.core.restclient.vambatch.VamBatchRestClient
import com.konai.vam.core.restclient.wooribank.WooriBankRestClient
import fixtures.*
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.mockk
import org.springframework.data.redis.core.RedisTemplate

abstract class CustomBehaviorSpec(body: BaseBehaviorSpec.() -> Unit = {}) : BaseBehaviorSpec() {
    init {
        body()
    }
}

abstract class BaseBehaviorSpec : BehaviorSpec() {

    private val wooriBankTransactionMapper = WooriBankTransactionMapper()

    private val parentAccountMapper = ParentAccountMapper()
    private val parentAccountEntityAdapter = ParentAccountEntityAdapterFixture()

    private val parentAccountSaveService = ParentAccountSaveService(parentAccountMapper, parentAccountEntityAdapter)
    private val parentAccountFindService = ParentAccountFindService(parentAccountMapper, parentAccountEntityAdapter)
    private val parentAccountDeleteService = ParentAccountDeleteService(parentAccountEntityAdapter)
    private val parentAccountManagementService = ParentAccountManagementService(parentAccountSaveService, parentAccountFindService, parentAccountDeleteService)

    private val virtualAccountEntityAdapter = VirtualAccountEntityAdapterFixture()
    private val virtualAccountMapper = VirtualAccountMapper(parentAccountMapper)

    private val virtualAccountWriteService = VirtualAccountWriteService(virtualAccountMapper, virtualAccountEntityAdapter, parentAccountFindService)
    private val virtualAccountFindService = VirtualAccountFindService(virtualAccountEntityAdapter, virtualAccountMapper)

    private val virtualAccountBankEntityAdapter = VirtualAccountBankEntityAdapterFixture()
    private val virtualAccountBankMapper = VirtualAccountBankMapper()
    private val virtualAccountBankFindService = VirtualAccountBankFindService(virtualAccountBankEntityAdapter, virtualAccountBankMapper)

    private val rechargeTransactionEntityAdapter = RechargeTransactionRepositoryFixture()
    private val rechargeTransactionMapper = RechargeTransactionMapper()
    private val rechargeTransactionSaveService = RechargeTransactionSaveService(rechargeTransactionEntityAdapter, rechargeTransactionMapper)
    private val rechargeTransactionFindService = RechargeTransactionFindService(rechargeTransactionEntityAdapter, rechargeTransactionMapper)

    private val mockNumberRedisTemplate = mockk<RedisTemplate<String, Number>>()
    private val redisTemplateService = RedisTemplateService(mockNumberRedisTemplate)
    private val wooriBankAggregationCacheService = WooriBankAggregationCacheService(redisTemplateService)

    private val wooriBankAggregationEntityAdapter = WooriBankAggregationEntityAdapterFixture()
    private val wooriBankAggregationMapper = WooriBankAggregationMapper()
    private val mockWooriBankRestClient = mockk<WooriBankRestClient>()

    private val sequenceGeneratorEntityAdapter = SequenceGeneratorEntityAdapterFixture()
    private val sequenceGeneratorService = SequenceGeneratorService(sequenceGeneratorEntityAdapter)
    private val wooriBankCommonMessageService = WooriBankMessageGenerateService(sequenceGeneratorService)

    private val wooriBankAggregationService = WooriBankAggregationService(wooriBankAggregationCacheService, wooriBankAggregationEntityAdapter, wooriBankCommonMessageService, wooriBankAggregationMapper, mockWooriBankRestClient)

    private val mockCsRestClient = mockk<CsRestClient>()

    private val rechargeTransactionService = RechargeTransactionService(rechargeTransactionMapper, rechargeTransactionSaveService, rechargeTransactionFindService, mockCsRestClient)
    private val wooriBankTransactionService = WooriBankTransactionService(wooriBankTransactionMapper, rechargeTransactionService, rechargeTransactionFindService, wooriBankAggregationService, virtualAccountFindService, virtualAccountBankFindService)

    private val wooriBankTransactionFixture = WooriBankTransactionFixture()
    private val rechargeTransactionFixture = RechargeTransactionFixture()
    private val rechargeTransactionEntityFixture = RechargeTransactionEntityFixture()
    private val wooriBankRestClientModelFixture = WooriBankRestClientModelFixture()

    private val wooriBankWorkService = WooriBankWorkService(wooriBankCommonMessageService, mockWooriBankRestClient)

    private val wooriBankManagementMapper = WooriBankManagementMapper()
    private val wooriBankManagementEntityAdapter = WooriBankManagementEntityAdapterFixture()
    private val wooriBankManagementFindService = WooriBankManagementFindService(wooriBankManagementMapper, wooriBankManagementEntityAdapter)
    private val wooriBankManagementSaveService = WooriBankManagementSaveService(wooriBankManagementMapper, wooriBankManagementEntityAdapter)
    private val wooriBankManagementService = WooriBankManagementService(wooriBankManagementMapper, wooriBankManagementFindService, wooriBankManagementSaveService, wooriBankTransactionService)
    private val wooriBankManagementFixture = WooriBankManagementFixture()

    private val mockVamBatchRestClient = mockk<VamBatchRestClient>()
    private val virtualAccountCardBatchService = VirtualAccountCardBatchService(mockVamBatchRestClient)

    private val wooriBankMessageFixture = WooriBankMessageFixture()
    private val virtualAccountFixture = VirtualAccountFixture()
    private val virtualAccountEntityFixture = VirtualAccountEntityFixture()

    private val parentAccountEntityFixture = ParentAccountEntityFixture()

    // service
    fun virtualAccountWriteService() = this.virtualAccountWriteService
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
    fun wooriBankCommonMessageService() = this.wooriBankCommonMessageService
    fun wooriBankWorkService() = this.wooriBankWorkService
    fun redisTemplateService() = this.redisTemplateService
    fun parentAccountManagementService() = this.parentAccountManagementService
    fun parentAccountFindService() = this.parentAccountFindService

    // entity adapter
    fun virtualAccountEntityAdapter() = this.virtualAccountEntityAdapter
    fun virtualAccountBankEntityAdapter() = this.virtualAccountBankEntityAdapter
    fun wooriBankAggregationEntityAdapter() = this.wooriBankAggregationEntityAdapter
    fun wooriBankManagementEntityAdapter() = this.wooriBankManagementEntityAdapter
    fun rechargeTransactionEntityAdapter() = this.rechargeTransactionEntityAdapter
    fun parentAccountEntityAdapter() = this.parentAccountEntityAdapter

    // mapper
    fun virtualAccountMapper() = this.virtualAccountMapper

    // etc
    fun mockCsRestClient() = this.mockCsRestClient
    fun mockWooriBankRestClient() = this.mockWooriBankRestClient
    fun mockNumberRedisTemplate() = this.mockNumberRedisTemplate
    fun mockVamBatchRestClient() = this.mockVamBatchRestClient

    // fixture
    fun virtualAccountFixture() = this.virtualAccountFixture
    fun virtualAccountEntityFixture() = this.virtualAccountEntityFixture
    fun wooriBankTransactionFixture() = this.wooriBankTransactionFixture
    fun rechargeTransactionFixture() = this.rechargeTransactionFixture
    fun rechargeTransactionEntityFixture() = this.rechargeTransactionEntityFixture
    fun wooriBankRestClientModelFixture() = this.wooriBankRestClientModelFixture
    fun wooriBankManagementFixture() = this.wooriBankManagementFixture
    fun wooriBankCommonMessageFixture() = this.wooriBankMessageFixture
    fun parentAccountEntityFixture() = this.parentAccountEntityFixture

}