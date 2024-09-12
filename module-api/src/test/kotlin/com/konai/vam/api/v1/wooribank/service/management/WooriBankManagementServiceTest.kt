package com.konai.vam.api.v1.wooribank.service.management

import com.konai.vam.api.v1.kotestspec.CustomBehaviorSpec
import com.konai.vam.api.v1.wooribank.cache.WooriBankAggregationCache
import com.konai.vam.core.common.WOORI_BANK_PREFIX
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.RestClientServiceException
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.CONNECTED
import com.konai.vam.core.enumerate.VirtualAccountStatus.ACTIVE
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.enumerate.WooriBankResponseCode.*
import com.konai.vam.core.enumerate.YesOrNo.Y
import com.konai.vam.core.repository.virtualaccountbank.VirtualAccountBankConst
import com.konai.vam.core.repository.wooribank.management.jdsl.WooriBankManagementPredicate
import com.konai.vam.core.restclient.cs.CsPostRechargesSystemManualsResponse
import com.konai.vam.core.restclient.cs.CsPostRechargesSystemManualsReversalResponse
import com.konai.vam.core.util.DATE_BASIC_PATTERN
import com.konai.vam.core.util.DATE_yyMMdd_PATTERN
import com.konai.vam.core.util.convertPatternOf
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import java.time.LocalDate
import java.util.*

class WooriBankManagementServiceTest : CustomBehaviorSpec({

    val wooriBankManagementService = wooriBankManagementService()

    val virtualAccountEntityAdaptor = virtualAccountEntityAdaptor()
    val virtualAccountBankEntityAdaptor = virtualAccountBankEntityAdaptor()
    val wooriBankManagementEntityAdapter = wooriBankManagementEntityAdapter()

    val mockCsRestClient = mockCsRestClient()
    val mockNumberRedisTemplate = mockNumberRedisTemplate()

    val wooriBankManagementFixture = wooriBankManagementFixture()
    val rechargeTransactionEntityAdaptor = rechargeTransactionEntityAdaptor()

    given("우리은행 가상 계좌 관리 전문 연동 요청되어") {
        `when`("전문 번호가 미정의된 요청으로 실패인 경우") {
            val messageNo = UUID.randomUUID().toString().substring(0, 6)
            val messageTypeCode = "9999"
            val businessTypeCode = "999"
            val transmissionDate = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN)

            val domain = wooriBankManagementFixture.make(messageTypeCode, businessTypeCode, messageNo, transmissionDate)
            val result = wooriBankManagementService.management(domain)

            then("'K401 - 전문 FORMAT 오류' responseCode 처리하여 응답 성공한다") {
                result.responseCode shouldBe K401
                result.responseMessage shouldBe "Woori bank message code is invalid"
            }

            then("우리은행 전문 연동 내역 DB 조회하여 정상 확인한다") {
                val predicate = WooriBankManagementPredicate(messageTypeCode, businessTypeCode, messageNo, transmissionDate)
                val entity = wooriBankManagementEntityAdapter.findByPredicate(predicate).orElse(null)
                entity shouldNotBe null
                entity.responseCode shouldBe K401
                entity.responseMessage shouldBe "Woori bank message code is invalid"
            }
        }

        `when`("동일한 '전문코드' & '전문번호' & '전송일자' 중복 연동 내역 정보 존재하는 경우") {
            val messageNo = UUID.randomUUID().toString().substring(0, 6)
            val messageType = WooriBankMessageType.VIRTUAL_ACCOUNT_INQUIRY
            val messageTypeCode = messageType.requestCode.messageTypeCode
            val businessTypeCode = messageType.requestCode.businessTypeCode
            val transmissionDate = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN)
            val domain = wooriBankManagementFixture.make(messageTypeCode, businessTypeCode, messageNo, transmissionDate)

            // 우리은행 전문 연동 내역 저장
            wooriBankManagementEntityAdapter.save(messageType.requestCode, messageNo, transmissionDate, `0000`)

            val result = wooriBankManagementService.management(domain)

            then("기존 전문 응답코드 '0000' 반환 처리하여 응답 성공한다") {
                result.messageTypeCode shouldBe messageType.responseCode.messageTypeCode
                result.businessTypeCode shouldBe messageType.responseCode.businessTypeCode
                result.responseCode shouldBe `0000`
            }

            then("우리은행 전문 연동 요청 내역 DB 조회하여 정상 확인한다") {
                val predicate = WooriBankManagementPredicate(messageTypeCode, businessTypeCode, messageNo, responseCode = `0000`)
                val entity = wooriBankManagementEntityAdapter.findByPredicate(predicate).orElse(null)
                entity.messageTypeCode shouldBe "0200"
                entity.businessTypeCode shouldBe "400"
                entity.responseCode shouldBe `0000`
                entity.transmissionDate shouldBe transmissionDate
            }
        }

        `when`("가상 계좌 '조회(0200-400)' 전문 요청 성공인 경우") {
            val messageNo = UUID.randomUUID().toString().substring(0, 6)
            val transmissionDate = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN)

            val domain = wooriBankManagementFixture.make("0200", "400", messageNo)
            val result = wooriBankManagementService.management(domain)

            then("가상 계좌 '조회' 응답 성공한다") {
                result.responseCode shouldBe `0000`
                result.messageTypeCode shouldBe "0210"
                result.businessTypeCode shouldBe "400"
            }

            then("우리은행 '조회(0200-400)' 전문 연동 요청 내역 DB 조회하여 정상 확인한다") {
                val predicate = WooriBankManagementPredicate("0200", "400", messageNo, transmissionDate, `0000`)
                val entity = wooriBankManagementEntityAdapter.findByPredicate(predicate).orElse(null)
                entity.messageTypeCode shouldBe "0200"
                entity.businessTypeCode shouldBe "400"
                entity.responseCode shouldBe `0000`
            }
        }

        `when`("가상 계좌 '입금(0200-600)' 전문 요청 성공인 경우") {
            val messageNo = UUID.randomUUID().toString().substring(0, 6)
            val transmissionDate = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN)
            val accountNo = UUID.randomUUID().toString()
            val messageTypeCode = WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT.requestCode.messageTypeCode
            val businessTypeCode = WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT.requestCode.businessTypeCode
            val domain = wooriBankManagementFixture.make(messageTypeCode, businessTypeCode, messageNo, transmissionDate, accountNo = accountNo)

            // 가상 계좌 정보 저장 처리
            virtualAccountEntityAdaptor.save(accountNo = accountNo, status = ACTIVE, cardConnectStatus = CONNECTED)
            // 가상 계좌 정보 저장 처리
            val par = "par$accountNo"
            val serviceId = "serviceId"
            virtualAccountEntityAdaptor.save(accountNo = accountNo, status = ACTIVE, cardConnectStatus = CONNECTED, par = par, serviceId = serviceId)
            // 가상 계좌 은행 정보 저장 처리
            val bank = VirtualAccountBankConst.woori
            val rechargerId = "rechargerId"
            virtualAccountBankEntityAdaptor.save(bank, rechargerId)
            // CS 시스템 충전 요청 처리
            val transactionId = "transactionId"
            val nrNumber = "nrNumber"
            every { mockCsRestClient.postRechargesSystemManuals(any()) } returns CsPostRechargesSystemManualsResponse(transactionId = transactionId, nrNumber = nrNumber)
            // 우리은행 집계 cache 정보 mocking 처리
            val aggregateDate = LocalDate.now().convertPatternOf()
            val count = 1L
            val amount = domain.trAmount.toLong()
            val cache = WooriBankAggregationCache(aggregateDate)
            every { mockNumberRedisTemplate.opsForValue().increment(cache.depositCountCacheKey, count) } returns count
            every { mockNumberRedisTemplate.opsForValue().increment(cache.depositAmountCacheKey, amount) } returns amount
            every { mockNumberRedisTemplate.opsForValue().multiGet(cache.keys) } returns listOf(count, amount, 0, 0)

            val result = wooriBankManagementService.management(domain)

            then("가상 계좌 입금(충전) 처리하여 응답 성공한다") {
                result.responseCode shouldBe `0000`
            }

            then("우리은행 '입금(0200-600)' 전문 연동 요청 내역 DB 조회하여 정상 확인한다") {
                val predicate = WooriBankManagementPredicate("0200", "600", messageNo, transmissionDate, `0000`)
                val entity = wooriBankManagementEntityAdapter.findByPredicate(predicate).orElse(null)
                entity.messageTypeCode shouldBe "0200"
                entity.businessTypeCode shouldBe "600"
                entity.responseCode shouldBe `0000`
            }

            then("우리은행 '입금(0200-600)' 전문 연동 요청 내역 1건 저장 정상 확인한다") {
                val predicate = WooriBankManagementPredicate("0200", "600", messageNo, transmissionDate)
                val entities = wooriBankManagementEntityAdapter.findAll(predicate)
                entities.size shouldBe 1
                entities.first().id shouldNotBe null
            }
        }

        `when`("가상 계좌 '입금 취소(0420-700)' 전문 요청 성공인 경우") {
            val messageNo = UUID.randomUUID().toString().substring(0, 6)
            val transmissionDate = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN)
            val messageType = WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT_CANCEL
            val messageTypeCode = messageType.requestCode.messageTypeCode
            val businessTypeCode = messageType.requestCode.businessTypeCode
            val orgMessageNo = UUID.randomUUID().toString().substring(0, 6)
            val domain = wooriBankManagementFixture.make(messageTypeCode, businessTypeCode, messageNo, transmissionDate, orgMessageNo = orgMessageNo)

            // 충전 내역 원거래 정보 저장
            rechargeTransactionEntityAdaptor.save(tranNo = "$WOORI_BANK_PREFIX${domain.trDate}$orgMessageNo", accountNo = domain.accountNo)
            // 가상 계좌 정보 저장 처리
            val accountNo = domain.accountNo
            val par = "par$accountNo"
            val serviceId = "serviceId"
            virtualAccountEntityAdaptor.save(accountNo = accountNo, status = ACTIVE, cardConnectStatus = CONNECTED, par = par, serviceId = serviceId)
            // CS 시스템 충전 요청 성공 mocking 처리
            every { mockCsRestClient.postRechargesSystemManualsReversal(any()) } returns CsPostRechargesSystemManualsReversalResponse(transactionId = "transactionId")
            // 우리은행 집계 cache 정보 mocking 처리
            val aggregateDate = LocalDate.now().convertPatternOf(DATE_BASIC_PATTERN)
            val count = 1L
            val amount = domain.trAmount.toLong()
            val cache = WooriBankAggregationCache(aggregateDate)
            every { mockNumberRedisTemplate.opsForValue().increment(cache.depositCancelCountCacheKey, count) } returns count
            every { mockNumberRedisTemplate.opsForValue().increment(cache.depositCancelAmountCacheKey, amount) } returns amount
            every { mockNumberRedisTemplate.opsForValue().multiGet(cache.keys) } returns listOf(0, 0, count, amount)

            val result = wooriBankManagementService.management(domain)

            then("가상 계좌 입금 취소(충전 취소) 처리하여 응답 성공한다") {
                result.responseCode shouldBe `0000`
            }

            then("우리은행 '입금 취소(0420-700)' 전문 연동 요청 내역 DB 조회하여 정상 확인한다") {
                val predicate = WooriBankManagementPredicate("0420", "700", messageNo, transmissionDate, `0000`)
                val entity = wooriBankManagementEntityAdapter.findByPredicate(predicate).orElse(null)
                entity.messageTypeCode shouldBe "0420"
                entity.businessTypeCode shouldBe "700"
                entity.responseCode shouldBe `0000`
                entity.messageNo shouldBe messageNo
                entity.orgMessageNo shouldBe orgMessageNo
            }
        }

        `when`("가상 계좌 '입금 자동 취소(0420-700)/(전문번호 = 원전문번호)' 전문 요청 성공인 경우") {
            val messageNo = UUID.randomUUID().toString().substring(0, 6)
            val transmissionDate = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN)
            val messageType = WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT_CANCEL
            val messageTypeCode = messageType.requestCode.messageTypeCode
            val businessTypeCode = messageType.requestCode.businessTypeCode
            val domain = wooriBankManagementFixture.make(messageTypeCode, businessTypeCode, messageNo, transmissionDate, orgMessageNo = messageNo)

            // 충전 내역 원거래 정보 저장
            rechargeTransactionEntityAdaptor.save(tranNo = "$WOORI_BANK_PREFIX${domain.trDate}$messageNo", accountNo = domain.accountNo)
            // 가상 계좌 정보 저장 처리
            val accountNo = domain.accountNo
            val par = "par$accountNo"
            val serviceId = "serviceId"
            virtualAccountEntityAdaptor.save(accountNo = accountNo, status = ACTIVE, cardConnectStatus = CONNECTED, par = par, serviceId = serviceId)
            // CS 시스템 충전 요청 성공 mocking 처리
            every { mockCsRestClient.postRechargesSystemManualsReversal(any()) } returns CsPostRechargesSystemManualsReversalResponse(transactionId = "transactionId")
            // 우리은행 집계 cache 정보 mocking 처리
            val aggregateDate = LocalDate.now().convertPatternOf(DATE_BASIC_PATTERN)
            val count = 1L
            val amount = domain.trAmount.toLong()
            val cache = WooriBankAggregationCache(aggregateDate)
            every { mockNumberRedisTemplate.opsForValue().increment(cache.depositCancelCountCacheKey, count) } returns count
            every { mockNumberRedisTemplate.opsForValue().increment(cache.depositCancelAmountCacheKey, amount) } returns amount
            every { mockNumberRedisTemplate.opsForValue().multiGet(cache.keys) } returns listOf(0, 0, count, amount)

            val result = wooriBankManagementService.management(domain)

            then("가상 계좌 입금 자동 취소(충전 취소) 처리하여 응답 성공한다") {
                result.responseCode shouldBe `0000`
            }

            then("우리은행 '입금 자동 취소(0420-700)' 전문 연동 요청 내역 DB 조회하여 정상 확인한다") {
                val predicate = WooriBankManagementPredicate("0420", "700", messageNo, transmissionDate, `0000`)
                val entity = wooriBankManagementEntityAdapter.findByPredicate(predicate).orElse(null)
                entity.messageTypeCode shouldBe "0420"
                entity.businessTypeCode shouldBe "700"
                entity.responseCode shouldBe `0000`
                entity.messageNo shouldBe messageNo
                entity.orgMessageNo shouldBe messageNo
            }
        }

        `when`("가상 계좌 '입금 확인 통보(0200-800)' 전문 요청인 경우") {
            val messageNo = UUID.randomUUID().toString().substring(0, 6)
            val transmissionDate = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN)
            val messageTypeCode = WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT_CONFIRM.requestCode.messageTypeCode
            val businessTypeCode = WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT_CONFIRM.requestCode.businessTypeCode
            val domain = wooriBankManagementFixture.make(messageTypeCode, businessTypeCode, messageNo, transmissionDate)

            // 충전 내역 원거래 '충전 실패' 정보 저장
            rechargeTransactionEntityAdaptor.save(tranNo = "$WOORI_BANK_PREFIX${domain.trDate}${domain.messageNo}", accountNo = domain.accountNo)

            val result = wooriBankManagementService.management(domain)

            then("가상 계좌 입금 확인 처리하여 응답 성공한다") {
                result.responseCode shouldBe `0000`
                result.depositConfirm shouldBe Y
            }

            then("우리은행 '입금 확인 통보(0200-800)' 전문 연동 요청 내역 DB 조회하여 정상 확인한다") {
                val predicate = WooriBankManagementPredicate("0200", "800", messageNo, transmissionDate, `0000`)
                val entity = wooriBankManagementEntityAdapter.findByPredicate(predicate).orElse(null)
                entity.messageTypeCode shouldBe "0200"
                entity.businessTypeCode shouldBe "800"
                entity.responseCode shouldBe `0000`
            }
        }
    }
    
    given("우리은행 가상 계좌 '자동 취소' 전문 연동 요청되어") {
        val messageNo = UUID.randomUUID().toString().substring(0, 6)
        val transmissionDate = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN)
        val messageType = WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT_CANCEL
        val messageTypeCode = messageType.requestCode.messageTypeCode
        val businessTypeCode = messageType.requestCode.businessTypeCode
        val domain = wooriBankManagementFixture.make(messageTypeCode, businessTypeCode, messageNo, transmissionDate, orgMessageNo = messageNo)

        // 충전 내역 원거래 정보 저장
        rechargeTransactionEntityAdaptor.save(tranNo = "$WOORI_BANK_PREFIX${domain.trDate}$messageNo", accountNo = domain.accountNo)
        // 가상 계좌 정보 저장 처리
        val accountNo = domain.accountNo
        val par = "par$accountNo"
        val serviceId = "serviceId"
        virtualAccountEntityAdaptor.save(accountNo = accountNo, status = ACTIVE, cardConnectStatus = CONNECTED, par = par, serviceId = serviceId)
        // CS 시스템 충전 요청 성공 mocking 처리
        every { mockCsRestClient.postRechargesSystemManualsReversal(any()) } throws RestClientServiceException(ErrorCode.VIRTUAL_ACCOUNT_RECHARGE_CANCEL_FAILED)

        `when`("가상 계좌 '입금 취소(0420-700)' 전문 요청 실패인 경우") {
            val result = wooriBankManagementService.management(domain)

            then("가상 계좌 '자동 취소' (반드시) 응답코드 '0000' 확인한다") {
                result.responseCode shouldBe `0000`
            }

            then("우리은행 연동 내역 확인하여 에러 사유 정상 확인한다") {
                val predicate = WooriBankManagementPredicate("0420", "700", messageNo, transmissionDate, K777)
                val entity = wooriBankManagementEntityAdapter.findByPredicate(predicate).orElse(null)
                entity.messageTypeCode shouldBe "0420"
                entity.businessTypeCode shouldBe "700"
                entity.messageNo shouldBe messageNo
                entity.orgMessageNo shouldBe messageNo
                entity.responseCode shouldNotBe `0000`
            }
        }
    }

})