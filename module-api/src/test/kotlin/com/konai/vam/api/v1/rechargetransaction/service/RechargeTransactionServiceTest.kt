package com.konai.vam.api.v1.rechargetransaction.service

import com.konai.vam.api.v1.kotestspec.CustomBehaviorSpec
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.RestClientServiceException
import com.konai.vam.core.enumerate.RechargeTransactionType.CANCEL
import com.konai.vam.core.enumerate.Result.FAILED
import com.konai.vam.core.enumerate.Result.SUCCESS
import com.konai.vam.core.restclient.cs.CsPostRechargesSystemManualsResponse
import com.konai.vam.core.restclient.cs.CsPostRechargesSystemManualsReversalResponse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import java.util.*

class RechargeTransactionServiceTest : CustomBehaviorSpec({

    val rechargeTransactionFixture = rechargeTransactionFixture()
    val rechargeTransactionEntityAdaptor = rechargeTransactionEntityAdaptor()
    val mockCsRestClient = mockCsRestClient()

    val rechargeTransactionService = rechargeTransactionService()

    given("CS 컴포넌트의 시스템 충전 API 요청하여") {
        val tranNo = UUID.randomUUID().toString()

        `when`("실패인 경우") {
            val domain = rechargeTransactionFixture.make(tranNo = tranNo)

            every { mockCsRestClient.postRechargesSystemManuals(any()) } throws RestClientServiceException(ErrorCode.VIRTUAL_ACCOUNT_RECHARGE_FAILED)

            val result = rechargeTransactionService.recharge(domain)

            then("시스템 충전 '실패' 거래 Entity 정보 생성하여 저장한다") {
                result shouldNotBe null
                result.result shouldBe FAILED
                result.reason shouldBe "[013] Virtual account recharge failed"
            }
        }

        `when`("성공인 경우") {
            val domain = rechargeTransactionFixture.make(tranNo = tranNo)
            val transactionId = "transactionId"
            val nrNumber = "nrNumber"

            every { mockCsRestClient.postRechargesSystemManuals(any()) } returns CsPostRechargesSystemManualsResponse(transactionId = transactionId, nrNumber = nrNumber)

            val result = rechargeTransactionService.recharge(domain)

            then("시스템 충전 '성공' 거래 Entity 정보 생성하여 저장한다") {
                result shouldNotBe null
                result.result shouldBe SUCCESS
                result.reason shouldBe null

                rechargeTransactionEntityAdaptor.findByTranNoAndTranType(tranNo)?.let {
                    it.result shouldBe SUCCESS
                    it.transactionId shouldBe transactionId
                    it.nrNumber shouldBe nrNumber
                }
            }
        }
    }

    given("CS 컴포넌트의 시스템 충전 취소 API 요청하여") {
        val tranNo = UUID.randomUUID().toString().substring(0, 6)
        val orgTranNo = UUID.randomUUID().toString().substring(0, 6)
        val domain = rechargeTransactionFixture.make(tranNo = tranNo, cancelOrgTranNo = orgTranNo, tranType = CANCEL)

        `when`("완료 거래 정보가 없는 경우") {

            val result = rechargeTransactionService.cancel(domain)

            then("시스템 충전 취소 거래 Entity '015(Recharge transaction not found)' 에러 정보 생성하여 저장한다") {
                result.tranType shouldBe  CANCEL
                result.result shouldBe FAILED
                result.reason shouldBe "[015] Recharge transaction not found"
                result.transactionId shouldBe null
            }
        }

        // 가상 계좌 충전 거래 entity 저장
        rechargeTransactionEntityAdaptor.save(tranNo = orgTranNo, accountNo = domain.bankAccount.accountNo, result = SUCCESS)

        `when`("완료 거래 기준 취소 API 요청하였지만 실패인 경우") {
            // CS 시스템 충전 요청 mocking 처리
            every { mockCsRestClient.postRechargesSystemManualsReversal(any()) } throws RestClientServiceException(ErrorCode.VIRTUAL_ACCOUNT_RECHARGE_CANCEL_FAILED)

            val result = rechargeTransactionService.cancel(domain)

            then("시스템 충전 취소 거래 Entity '014(Virtual account recharge cancel failed)' 에러 정보 생성하여 저장한다") {
                result.tranType shouldBe  CANCEL
                result.result shouldBe FAILED
                result.reason shouldBe "[014] Virtual account recharge cancel failed"
                result.transactionId shouldBe null
            }
        }

        `when`("완료 거래 기준 취소 API 요청하여 성공인 경우") {
            // CS 시스템 충전 요청 mocking 처리
            every { mockCsRestClient.postRechargesSystemManualsReversal(any()) } returns CsPostRechargesSystemManualsReversalResponse(transactionId = domain.transactionId)

            val result = rechargeTransactionService.cancel(domain)

            then("시스템 충전 취소 거래 Entity 성공 정보 생성하여 저장한다") {
                result.tranType shouldBe CANCEL
                result.result shouldBe SUCCESS
                result.reason shouldBe null
            }
        }

    }

})