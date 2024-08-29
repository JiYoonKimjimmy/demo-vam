package com.konai.vam.api.v1.wooribank.cache

import com.konai.vam.api.v1.kotestspec.CustomBehaviorSpec
import com.konai.vam.core.enumerate.RechargeTransactionType.CANCEL
import com.konai.vam.core.enumerate.RechargeTransactionType.RECHARGE
import com.konai.vam.core.util.DATE_BASIC_PATTERN
import com.konai.vam.core.util.convertPatternOf
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import io.mockk.every
import java.time.LocalDate

class WooriBankAggregationCacheServiceTest : CustomBehaviorSpec({

    val mockNumberRedisTemplate = mockNumberRedisTemplate()
    val redisTemplateService = redisTemplateService()
    val wooriBankAggregationCacheService = wooriBankAggregationCacheService()

    given("우리은행 집계 Cache 정보 조회 일자 정보를 입력하여") {
        val aggregateDate = LocalDate.now().convertPatternOf(DATE_BASIC_PATTERN)
        val cache = WooriBankAggregationCache(aggregateDate)
        val keys = cache.keys
        val values = listOf(10L, 1000L, 5L, 500L)

        every { mockNumberRedisTemplate.opsForValue().multiGet(any()) } returns values

        `when`("정상 Cache 정보 조회된 경우") {
            val result = redisTemplateService.getNumberMultiValues(keys)

            then("조회 결과를 확인한다") {
                result[0] shouldBe 10
                result[1] shouldBe 1000
                result[2] shouldBe 5
                result[3] shouldBe 500
            }
        }
    }

    given("'RECHARGE' 거래에 대한 우리은행 집계 Cache 정보 업데이트 요청하여") {
        val aggregateDate = LocalDate.now().convertPatternOf(DATE_BASIC_PATTERN)
        val cache = WooriBankAggregationCache(aggregateDate)
        val count = 1L
        val amount = 10000L

        every { mockNumberRedisTemplate.opsForValue().increment(cache.depositCountCacheKey, count) } returns count
        every { mockNumberRedisTemplate.opsForValue().increment(cache.depositAmountCacheKey, amount) } returns amount
        every { mockNumberRedisTemplate.opsForValue().multiGet(cache.keys) } returns listOf(count, amount, 0, 0)

        `when`("'depositCountCacheKey', 'depositAmountCacheKey' cache key 의 정보를 증가하여 성공인 경우") {
            wooriBankAggregationCacheService.incremantAggregationCache(aggregateDate, amount, RECHARGE)

            then("증가시킨 업데이트 정보를 확인한다") {
                val result = wooriBankAggregationCacheService.findAggregationCache(aggregateDate)
                result.konaDepositCount shouldBeGreaterThanOrEqual 1
                result.konaDepositAmount shouldBeGreaterThanOrEqual amount
            }
        }
    }

    given("'CANCLE' 거래에 대한 우리은행 집계 Cache 정보 업데이트 요청하여") {
        val aggregateDate = LocalDate.now().convertPatternOf(DATE_BASIC_PATTERN)
        val cache = WooriBankAggregationCache(aggregateDate)
        val count = 1L
        val amount = 10000L

        every { mockNumberRedisTemplate.opsForValue().increment(cache.depositCancelCountCacheKey, count) } returns count
        every { mockNumberRedisTemplate.opsForValue().increment(cache.depositCancelAmountCacheKey, amount) } returns amount
        every { mockNumberRedisTemplate.opsForValue().multiGet(cache.keys) } returns listOf(0, 0, count, amount)

        `when`("'depositCancelCountCacheKey', 'depositCancelAmountCacheKey' cache key 의 정보를 증가하여 성공인 경우") {
            wooriBankAggregationCacheService.incremantAggregationCache(aggregateDate, amount, CANCEL)

            then("증가시킨 업데이트 정보를 확인한다") {
                val result = wooriBankAggregationCacheService.findAggregationCache(aggregateDate)
                result.konaDepositCancelCount shouldBeGreaterThanOrEqual 1
                result.konaDepositCancelAmount shouldBeGreaterThanOrEqual amount
            }
        }
    }

})