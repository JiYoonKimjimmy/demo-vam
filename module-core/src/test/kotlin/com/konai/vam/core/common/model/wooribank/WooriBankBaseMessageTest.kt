package com.konai.vam.core.common.model.wooribank

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.konai.vam.core.enumerate.WooriBankMessage
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class WooriBankBaseMessageTest : BehaviorSpec({
    val objectMapper = ObjectMapper().registerModule(kotlinModule())

    given("'WooriBankBasicMessage' 전문 데이터를") {
        val message = WooriBankBasicMessage(
            messageTypeCode = WooriBankMessage.WORK_START.requestCode.messageTypeCode,
            businessTypeCode = WooriBankMessage.WORK_START.requestCode.businessTypeCode,
            messageNo = "123456",
        )

        `when`("직렬화 & 역직렬화 변환 테스트 성공인 경우") {
            val json = objectMapper.writeValueAsString(message)
            val result = objectMapper.readValue(json, WooriBankBaseMessage::class.java)

            then("원본 데이터와 일치 여부 정상 확인한다") {
                result.shouldBeInstanceOf<WooriBankBasicMessage>()
                result shouldBe message
            }
        }
    }

    given("'WooriBankAggregationMessage' 전문 데이터를") {
        val message = WooriBankAggregationMessage(
            messageTypeCode = WooriBankMessage.TRANSACTION_AGGREGATION.requestCode.messageTypeCode,
            businessTypeCode = WooriBankMessage.TRANSACTION_AGGREGATION.requestCode.businessTypeCode,
            messageNo = "123456",
            aggregationDate = "20240829",
            konaDepositCount = 10,
            konaDepositAmount = 1000000L,
            konaDepositCancelCount = 2,
            konaDepositCancelAmount = 200000L,
            konaDepositTrAmount = 800000L
        )

        `when`("직렬화 & 역직렬화 변환 테스트 성공인 경우") {
            val json = objectMapper.writeValueAsString(message)
            val result = objectMapper.readValue(json, WooriBankBaseMessage::class.java)

            then("원본 데이터와 일치 여부 정상 확인한다") {
                result.shouldBeInstanceOf<WooriBankAggregationMessage>()
                result shouldBe message
            }
        }
    }

})