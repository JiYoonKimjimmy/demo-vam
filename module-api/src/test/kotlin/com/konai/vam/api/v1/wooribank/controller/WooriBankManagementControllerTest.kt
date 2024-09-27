package com.konai.vam.api.v1.wooribank.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.konai.vam.api.testsupport.CustomBehaviorSpec
import com.konai.vam.core.common.WOORI_BANK_PREFIX
import com.konai.vam.core.enumerate.RechargeTransactionCancelStatus
import com.konai.vam.core.enumerate.RechargeTransactionType.CANCEL
import com.konai.vam.core.enumerate.RechargeTransactionType.RECHARGE
import com.konai.vam.core.enumerate.Result.SUCCESS
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.repository.rechargetransaction.RechargeTransactionRepository
import com.konai.vam.core.util.convertPatternOf
import fixtures.WooriBankManagementRequestFixture
import fixtures.TestExtensionFunctions.generateUUID
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalTime

@AutoConfigureMockMvc
@SpringBootTest
class WooriBankManagementControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val rechargeTransactionRepository: RechargeTransactionRepository
) : CustomBehaviorSpec({

    val wooriBankManagementRequestFixture = WooriBankManagementRequestFixture()
    val objectMapper = jacksonObjectMapper()

    given("우리은행 가상 계좌 '입금' & '입금 취소' 전문 요청되어") {
        val messageNo = generateUUID(6)
        val accountNo = "94000016118257"
        val trAmount = 200000
        val trDate = LocalDate.now().convertPatternOf()
        val trTime = LocalTime.now().convertPatternOf()

        `when`("정상 신규 '입금' 전문인 경우") {
            val request = wooriBankManagementRequestFixture.make(
                messageTypeCode = WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT.requestCode.messageTypeCode,
                businessTypeCode = WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT.requestCode.businessTypeCode,
                messageNo = messageNo,
                trDate = trDate,
                trTime = trTime,
                accountNo = accountNo,
                trAmount = trAmount
            )

            mockMvc.perform(
                post("/api/v1/woori/virtual-account/management")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andDo(print())
                .andExpect(status().isOk)

            then("카드 충전 완료 성공한다") {
                // 충전 거래 내역 조회 검증
                println("${request.trDate}${request.trTime}${request.messageNo}")
                val successResult = rechargeTransactionRepository.findByTranNoAndAccountNoAndTranTypeAndResult(
                    tranNo = "$WOORI_BANK_PREFIX${request.trDate}${request.messageNo}",
                    accountNo = accountNo,
                    tranType = RECHARGE,
                    result = SUCCESS
                )
                successResult shouldNotBe null
            }
        }

        `when`("정상 '입금 취소' 전문인 경우") {
            val request = wooriBankManagementRequestFixture.make(
                messageTypeCode = WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT_CANCEL.requestCode.messageTypeCode,
                businessTypeCode = WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT_CANCEL.requestCode.businessTypeCode,
                messageNo = generateUUID(6),
                trDate = trDate,
                trTime = trTime,
                accountNo = accountNo,
                trAmount = trAmount,
                orgMessageNo = messageNo
            )

            mockMvc.perform(
                post("/api/v1/woori/virtual-account/management")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andDo(print())
                .andExpect(status().isOk)

            then("카드 충전 취소 완료 성공한다") {
                // 충전 취소 거래 내역 조회 검증
                val successResult = rechargeTransactionRepository.findByTranNoAndAccountNoAndTranTypeAndResult(
                    tranNo = "$WOORI_BANK_PREFIX${request.trDate}${request.orgMessageNo}",
                    accountNo = accountNo,
                    tranType = RECHARGE,
                    result = SUCCESS
                )
                successResult shouldNotBe null
                successResult?.cancelStatus shouldBe RechargeTransactionCancelStatus.CANCEL

                val cancelResult = rechargeTransactionRepository.findByTranNoAndAccountNoAndTranTypeAndResult(
                    tranNo = "$WOORI_BANK_PREFIX${request.trDate}${request.messageNo}",
                    accountNo = accountNo,
                    tranType = CANCEL,
                    result = SUCCESS
                )
                cancelResult shouldNotBe null
            }
        }
    }

})