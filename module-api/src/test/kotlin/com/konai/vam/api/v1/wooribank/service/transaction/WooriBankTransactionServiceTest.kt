package com.konai.vam.api.v1.wooribank.service.transaction

import com.konai.vam.api.v1.kotestspec.CustomBehaviorSpec
import com.konai.vam.api.v1.wooribank.cache.WooriBankAggregationCache
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.error.exception.RestClientServiceException
import com.konai.vam.core.enumerate.RechargeTransactionCancelStatus
import com.konai.vam.core.enumerate.RechargeTransactionType.CANCEL
import com.konai.vam.core.enumerate.RechargeTransactionType.RECHARGE
import com.konai.vam.core.enumerate.Result.FAILED
import com.konai.vam.core.enumerate.Result.SUCCESS
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.CONNECTED
import com.konai.vam.core.enumerate.VirtualAccountStatus.ACTIVE
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.enumerate.WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT_CANCEL
import com.konai.vam.core.enumerate.WooriBankResponseCode.*
import com.konai.vam.core.enumerate.YesOrNo.Y
import com.konai.vam.core.repository.virtualaccountbank.VirtualAccountBankConst
import com.konai.vam.core.restclient.cs.CsPostRechargesSystemManualsResponse
import com.konai.vam.core.restclient.cs.CsPostRechargesSystemManualsReversalResponse
import com.konai.vam.core.util.DATE_BASIC_PATTERN
import com.konai.vam.core.util.convertPatternOf
import io.kotest.matchers.shouldBe
import io.mockk.every
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class WooriBankTransactionServiceTest : CustomBehaviorSpec({

    val wooriBankTransactionService = wooriBankTransactionService()
    val wooriBankAggregationCacheService = wooriBankAggregationCacheService()

    val virtualAccountEntityAdaptor = virtualAccountEntityAdaptor()
    val virtualAccountBankEntityAdaptor = virtualAccountBankEntityAdaptor()

    val wooriBankTransactionFixture = wooriBankTransactionFixture()
    val rechargeTransactionEntityAdaptor = rechargeTransactionEntityAdaptor()

    val mockCsRestClient = mockCsRestClient()

    val mockNumberRedisTemplate = mockNumberRedisTemplate()

    given("우리은행 가상 계좌 '입금' 신규 전문 요청되어") {
        val messageNo = UUID.randomUUID().toString().substring(0, 6)
        val accountNo = UUID.randomUUID().toString()
        val domain = wooriBankTransactionFixture.make(messageNo = messageNo, accountNo = accountNo)

        `when`("가상 계좌 정보가 등록되지 않은 경우") {
            val result = wooriBankTransactionService.deposit(domain)

            then("입금 전문 응답코드 'K402', 'Virtual account not found' 에러 사유 설정되어 반환한다") {
                result.responseCode shouldBe K402
                result.responseMessage shouldBe "Virtual account not found"
            }
        }

        // 가상 계좌 정보 저장 처리
        virtualAccountEntityAdaptor.save(accountNo = accountNo, status = ACTIVE)

        `when`("가상 계좌 정보는 있지만 연결 카드가 등록되지 않은 경우") {
            val result = wooriBankTransactionService.deposit(domain)

            then("입금 전문 응답코드 'K419', 'Virtual account has not connected card' 에러 사유 설정되어 반환한다") {
                result.responseCode shouldBe K419
                result.responseMessage shouldBe "Virtual account has not connected card"
            }
        }

        // 가상 계좌 정보 저장 처리
        val par = "par$accountNo"
        val serviceId = "serviceId"
        virtualAccountEntityAdaptor.save(accountNo = accountNo, status = ACTIVE, cardConnectStatus = CONNECTED, par = par, serviceId = serviceId)

        `when`("가상 계좌 은행 정보가 등록되지 않은 경우") {
            val result = wooriBankTransactionService.deposit(domain)

            then("입금 전문 응답코드 'K116', 'Virtual account bank not found' 에러 사유 설정되어 반환한다") {
                result.responseCode shouldBe K116
                result.responseMessage shouldBe "Virtual account bank not found"
            }
        }

        // 가상 계좌 은행 정보 저장 처리
        val bank = VirtualAccountBankConst.woori
        val rechargerId = "rechargerId"
        virtualAccountBankEntityAdaptor.save(bank, rechargerId)

        // CS 시스템 충전 요청 실패 mocking 처리
        every { mockCsRestClient.postRechargesSystemManuals(any()) } throws InternalServiceException(ErrorCode.VIRTUAL_ACCOUNT_RECHARGE_FAILED)

        `when`("시스템 충전 요청 결과가 실패인 경우") {
            val result = wooriBankTransactionService.deposit(domain)

            then("입금 전문 응답코드 'K116', 'Virtual account recharge failed' 에러 사유 설정되어 반환한다") {
                result.responseCode shouldBe K116
                result.responseMessage shouldBe "Virtual account recharge failed"
            }
        }

        // CS 시스템 충전 요청 성공 mocking 처리
        val transactionId = "transactionId"
        val nrNumber = "nrNumber"
        every { mockCsRestClient.postRechargesSystemManuals(any()) } returns CsPostRechargesSystemManualsResponse(transactionId = transactionId, nrNumber = nrNumber)

        // 우리은행 집계 cache 정보 mocking 처리
        val aggregateDate = LocalDate.now().convertPatternOf(DATE_BASIC_PATTERN)
        val count = 1L
        val amount = domain.trAmount
        val cache = WooriBankAggregationCache(aggregateDate)
        every { mockNumberRedisTemplate.opsForValue().increment(cache.depositCountCacheKey, count) } returns count
        every { mockNumberRedisTemplate.opsForValue().increment(cache.depositAmountCacheKey, amount) } returns amount
        every { mockNumberRedisTemplate.opsForValue().multiGet(cache.keys) } returns listOf(count, amount, 0, 0)

        `when`("가상 계좌 정보 & 연결 카드 & 은행 정보 & 충전 결과가 정상인 경우") {
            val result = wooriBankTransactionService.deposit(domain)

            then("입금 전문 응답코드 '0000' 성공 결과 설정되어 반환한다") {
                result.responseCode shouldBe `0000`
                result.responseMessage shouldBe null
            }

            then("우리은행 입금 내역 집계 Cache 정보 조회하여 정상 반영 확인한다") {
                val cacheResult = wooriBankAggregationCacheService.findAggregationCache(aggregateDate)
                cacheResult.konaDepositCount shouldBe count.toInt()
                cacheResult.konaDepositAmount shouldBe amount
            }
        }
    }

    given("우리은행 가상 계좌 '입금 취소' 전문 요청되어") {
        val messageCode = VIRTUAL_ACCOUNT_DEPOSIT_CANCEL.requestCode
        val trDate = LocalDate.now().convertPatternOf()
        val trTime = LocalTime.now().convertPatternOf()
        val messageNo = UUID.randomUUID().toString().substring(0, 6)
        val orgMessageNo = UUID.randomUUID().toString().substring(0, 6)
        val accountNo = UUID.randomUUID().toString()

        val domain = wooriBankTransactionFixture.make(messageCode = messageCode, messageNo = messageNo, orgMessageNo = orgMessageNo, accountNo = accountNo, trDate = trDate, trTime = trTime)

        `when`("'orgMessageNo' 기준 충전 거래 정보 존재하지 않는 경우") {
            val result = wooriBankTransactionService.depositCancel(domain)

            then("입금 취소 전문 응답코드 'K701', 'Recharge transaction not found' 에러 사유 설정되어 반환한다") {
                result.responseCode shouldBe K701
            }
        }

        // 충전 내역 원거래 취소 완료 정보 저장
        rechargeTransactionEntityAdaptor.save(tranNo = domain.orgTranNo, accountNo = accountNo, cancelStatus = RechargeTransactionCancelStatus.CANCEL)

        `when`("'orgMessageNo' 기준 원거래 정보가 이미 취소된 경우") {
            val result = wooriBankTransactionService.depositCancel(domain)

            then("입금 취소 전문 응답코드 'K408', 'This recharge transaction has already been canceled' 에러 사유 설정되어 반환한다") {
                result.responseCode shouldBe K408
            }
        }

        // 충전 내역 원거래 정보 저장
        rechargeTransactionEntityAdaptor.save(tranNo = "$trDate$trTime$orgMessageNo", accountNo = accountNo)

        `when`("'accountNo' 기준 가상 계좌 정보 없는 경우") {
            val result = wooriBankTransactionService.depositCancel(domain)

            then("입금 취소 전문 응답코드 'K402', 'Virtual account not found' 에러 사유 설정되어 반환한다") {
                result.responseCode shouldBe K402
            }
        }

        // 가상 계좌 정보 저장 처리
        virtualAccountEntityAdaptor.save(accountNo = accountNo, status = ACTIVE)

        `when`("'accountNo' 기준 가상 계좌 카드 미연결 상태인 경우") {
            val result = wooriBankTransactionService.depositCancel(domain)

            then("입금 취소 전문 응답코드 'K419', 'Virtual account has not connected card' 에러 사유 설정되어 반환한다") {
                result.responseCode shouldBe K419
            }
        }

        // 가상 계좌 정보 저장 처리
        val par = "par$accountNo"
        val serviceId = "serviceId"
        virtualAccountEntityAdaptor.save(accountNo = accountNo, status = ACTIVE, cardConnectStatus = CONNECTED, par = par, serviceId = serviceId)

        // CS 시스템 충전 요청 실패 mocking 처리
        every { mockCsRestClient.postRechargesSystemManualsReversal(any()) } throws RestClientServiceException(ErrorCode.VIRTUAL_ACCOUNT_RECHARGE_CANCEL_FAILED)

        `when`("원거래 정보 'transactionId' 기준 CS 충전 취소 요청 실패하는 경우") {
            val result = wooriBankTransactionService.depositCancel(domain)
            
            then("입금 취소 전문 응답코드 'K777', 'Recharge transaction cancel failed' 에러 사유 설정되어 반환한다") {
                result.responseCode shouldBe K777
            }

            then("충전 취소 내역 DB 정상 확인한다") {
                val entityResult = rechargeTransactionEntityAdaptor.findByTranNoAndTranType(domain.tranNo, CANCEL)!!
                entityResult.result shouldBe FAILED
                entityResult.reason shouldBe "[014] Virtual account recharge cancel failed"
            }
        }

        // CS 시스템 충전 요청 성공 mocking 처리
        val transactionId = "transactionId"
        every { mockCsRestClient.postRechargesSystemManualsReversal(any()) } returns CsPostRechargesSystemManualsReversalResponse(transactionId = transactionId)

        // 우리은행 집계 cache 정보 mocking 처리
        val aggregateDate = LocalDate.now().convertPatternOf(DATE_BASIC_PATTERN)
        val count = 1L
        val amount = domain.trAmount
        val cache = WooriBankAggregationCache(aggregateDate)
        every { mockNumberRedisTemplate.opsForValue().increment(cache.depositCancelCountCacheKey, count) } returns count
        every { mockNumberRedisTemplate.opsForValue().increment(cache.depositCancelAmountCacheKey, amount) } returns amount
        every { mockNumberRedisTemplate.opsForValue().multiGet(cache.keys) } returns listOf(0, 0, count, amount)

        `when`("CS 충전 취소 요청 성공하는 경우") {
            val result = wooriBankTransactionService.depositCancel(domain)

            then("입금 취소 전문 응답코드 '0000' 성공 결과 설정되어 반환한다") {
                result.responseCode shouldBe `0000`
            }

            then("원거래 기준 충전 내역 취소 처리 DB 정상 확인한다") {
                val entityResult = rechargeTransactionEntityAdaptor.findByTranNoAndTranType(domain.orgTranNo)!!
                entityResult.result shouldBe SUCCESS
                entityResult.tranType shouldBe RECHARGE
                entityResult.cancelStatus shouldBe RechargeTransactionCancelStatus.CANCEL
            }

            then("충전 취소 내역 DB 정상 확인한다") {
                val entityResult = rechargeTransactionEntityAdaptor.findByTranNoAndTranType(domain.tranNo, CANCEL)!!
                entityResult.result shouldBe SUCCESS
                entityResult.tranType shouldBe CANCEL
            }

            then("우리은행 입금 취소 내역 집계 Cache 정보 조회하여 정상 반영 확인한다") {
                val cacheResult = wooriBankAggregationCacheService.findAggregationCache(aggregateDate)
                cacheResult.konaDepositCancelCount shouldBe count.toInt()
                cacheResult.konaDepositCancelAmount shouldBe amount
            }
        }
    }

    given("우리은행 가상 계좌 '입금 확인 통보' 전문 요청되어") {
        val messageCode = WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT_CONFIRM.requestCode
        val messageNo = UUID.randomUUID().toString().substring(0, 6)
        val domain = wooriBankTransactionFixture.make(messageCode = messageCode, messageNo = messageNo)

        `when`("'messageNo' 기준 충전 거래 정보 존재하지 않는 경우") {
            val result = wooriBankTransactionService.depositConfirm(domain)

            then("입금 확인 통보 전문 응답코드 'K701', 'Recharge transaction not found' 에러 사유 설정되어 반환한다") {
                result.responseCode shouldBe K701
                result.responseMessage shouldBe "Recharge transaction not found"
            }
        }

        // 충전 내역 원거래 '충전 취소' 정보 저장
        rechargeTransactionEntityAdaptor.save(tranNo = domain.tranNo, accountNo = domain.accountNo, tranType = CANCEL)

        `when`("'messageNO' 기준 원거래 정보가 '충전 취소' 거래만 있는 경우") {
            val result = wooriBankTransactionService.depositConfirm(domain)

            then("입금 확인 통보 전문 응답코드 'K401', 'This recharge confirm transaction is invalid' 에러 사유 설정되어 반환한다") {
                result.responseCode shouldBe K701
                result.responseMessage shouldBe "Recharge transaction not found"
            }
        }

        // 충전 내역 원거래 '충전 실패' 정보 저장
        rechargeTransactionEntityAdaptor.save(tranNo = domain.tranNo, accountNo = domain.accountNo, tranType = CANCEL, result = FAILED)

        `when`("'messageNO' 기준 원거래 정보가 '충전 실패' 거래만 있는 경우") {
            val result = wooriBankTransactionService.depositConfirm(domain)

            then("입금 확인 통보 전문 응답코드 'K401', 'This recharge confirm transaction is invalid' 에러 사유 설정되어 반환한다") {
                result.responseCode shouldBe K701
                result.responseMessage shouldBe "Recharge transaction not found"
            }
        }

        // 충전 내역 원거래 '충전 실패' 정보 저장
        rechargeTransactionEntityAdaptor.save(tranNo = domain.tranNo, accountNo = domain.accountNo, cancelStatus = RechargeTransactionCancelStatus.CANCEL)

        `when`("'messageNo' 기준 원거래 정보가 이미 취소된 경우") {
            val result = wooriBankTransactionService.depositConfirm(domain)

            then("입금 확인 통보 전문 응답코드 'K408', 'This recharge transaction has already been canceled' 에러 사유 설정되어 반환한다") {
                result.responseCode shouldBe K408
                result.responseMessage shouldBe "This recharge transaction has already been canceled"
            }
        }

        // 충전 내역 원거래 '충전 실패' 정보 저장
        rechargeTransactionEntityAdaptor.save(tranNo = domain.tranNo, accountNo = domain.accountNo)

        `when`("'messageNo' 기준 원거래 충전 결과 정상 확인되는 경우") {
            val result = wooriBankTransactionService.depositConfirm(domain)

            then("입금 확인 통보 'Y' 결과 설정되어 반환한다") {
                result.depositConfirm shouldBe Y
            }
        }

    }

})