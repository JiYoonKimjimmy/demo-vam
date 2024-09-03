package com.konai.vam.api.v1.wooribank.service.aggregation

import com.konai.vam.api.v1.kotestspec.CustomBehaviorSpec
import com.konai.vam.api.v1.wooribank.cache.WooriBankAggregationCache
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.error.exception.RestClientServiceException
import com.konai.vam.core.enumerate.WooriBankAggregateResult.*
import com.konai.vam.core.enumerate.WooriBankMessageType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every

class WooriBankAggregationServiceTest : CustomBehaviorSpec({

    val wooriBankAggregationService = wooriBankAggregationService()

    val wooriBankAggregationCacheService = wooriBankAggregationCacheService()
    val wooriBankAggregationEntityAdapter = wooriBankAggregationEntityAdapter()
    val wooriBankCommonMessageService = wooriBankCommonMessageService()

    val mockNumberRedisTemplate = mockNumberRedisTemplate()
    val mockWooriBankRestClient = mockWooriBankRestClient()

    val wooriBankRestClientModelFixture = wooriBankRestClientModelFixture()

    given("'20240628' 일자 우리은행 거래내역 집계 동기화 요청을 하여") {
        val aggregationDate = "20240628"

        `when`("요청 일자의 집계 Cache 정보가 없는 경우") {
            every { mockNumberRedisTemplate.opsForValue().multiGet(any()) } returns emptyList()

            val exception = shouldThrow<ResourceNotFoundException> { wooriBankAggregationService.aggregateTransaction(aggregationDate) }

            then("'WOORI_BANK_AGGREGATION_CACHE_NOT_FOUND' 예외 발생하여 실패한다") {
                exception.errorCode shouldBe ErrorCode.WOORI_BANK_AGGREGATION_CACHE_NOT_FOUND
            }
        }

        // 우리은행 집계 Cache 정보 mocking 처리
        val depositCount = 10
        val depositAmount = 1000000
        every { mockNumberRedisTemplate.opsForValue().multiGet(any()) } returns listOf(depositCount, depositAmount, 0, 0)

        `when`("요청 일자의 집계 Cache 정보 기준 우리은행 집계 조회 요청 결과 실패인 경우") {
            // 우리은행 집계 조회 mocking 처리
            every { mockWooriBankRestClient.postWooriAggregateTransaction(any()) } throws RestClientServiceException(ErrorCode.EXTERNAL_SERVICE_ERROR)

            shouldThrow<RestClientServiceException> { wooriBankAggregationService.aggregateTransaction(aggregationDate) }

            then("우리은행 집계 Cache 정보 조회 성공한다") {
                val cache = wooriBankAggregationCacheService.findAggregationCache(aggregationDate)
                cache.konaDepositCount shouldBe depositCount
                cache.konaDepositAmount shouldBe depositAmount
            }

            then("우리은행 집계 'WAITING' 상태 정보 저장 성공한다") {
                val entity = wooriBankAggregationEntityAdapter.findByAggregateDate(aggregationDate)
                entity.aggregateResult shouldBe WAITING
            }
        }

        val message = wooriBankCommonMessageService.generateMessage(WooriBankMessageType.TRANSACTION_AGGREGATION.requestCode)

        `when`("요청 일자의 집계 Cache 정보 기준 우리은행 집계 조회 요청 결과 집계 데이터가 일치하지 않은 경우") {
            val response = wooriBankRestClientModelFixture.makePostWooriAggregateTransactionResponse(message.messageNo, aggregationDate)

            every { mockWooriBankRestClient.postWooriAggregateTransaction(any()) } returns response

            val result = wooriBankAggregationService.aggregateTransaction(aggregationDate)

            then("우리은행 집계 처리 결과 'MISMATCHED' 상태 정보 변경 성공한다") {
                result.aggregateResult shouldBe MISMATCHED

                val entity = wooriBankAggregationEntityAdapter.findByAggregateDate(aggregationDate)
                entity.aggregateResult shouldBe MISMATCHED
            }

            then("우리은행 가상계좌 집계 데이터 불일치 내용 Knotify 통해서 담당자에게 알림 발송 성공한다") {
                // TODO("알림 발송 처리 테스트 코드 추가 !!")
            }
        }

        // 우리은행 집계 조회 요청 mocking 처리
        val response = wooriBankRestClientModelFixture.makePostWooriAggregateTransactionResponse(
            messageNo = message.messageNo,
            aggregationDate = aggregationDate,
            bankDepositCount = depositCount,
            bankDepositAmount = depositAmount.toLong(),
            bankDepositTrAmount = depositAmount.toLong()
        )
        every { mockWooriBankRestClient.postWooriAggregateTransaction(any()) } returns response

        // 우리은행 집계 Cache 정보 삭제 mocking 처리
        val keys = WooriBankAggregationCache(aggregationDate).keys
        every { mockNumberRedisTemplate.delete(keys) } returns keys.size.toLong()

        `when`("요청 일자의 집계 Cache 정보 기준 우리은행 집계 조회 요청 결과 집계 데이터가 일치하는 경우") {
            val result = wooriBankAggregationService.aggregateTransaction(aggregationDate)

            then("우리은행 집계 'MATCHED' 상태 정보 변경 성공한다") {
                result.aggregateResult shouldBe MATCHED

                val entity = wooriBankAggregationEntityAdapter.findByAggregateDate(aggregationDate)
                entity.aggregateResult shouldBe MATCHED
            }
        }

    }

})