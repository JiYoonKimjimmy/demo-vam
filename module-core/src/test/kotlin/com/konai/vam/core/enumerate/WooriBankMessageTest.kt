package com.konai.vam.core.enumerate

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class WooriBankMessageTest : BehaviorSpec({

    given("가상 계좌 '조회' 전문 연동 요청에 대한") {
        val messageTypeCode = "0200"
        val businessTypeCode = "400"

        `when`("'0200', '400' 전문 요청 코드와 일치한 Enum 클래스 생성하여") {
            val result = WooriBankMessageType.find(messageTypeCode, businessTypeCode)

            then("정상 확인한다") {
                result.requestCode.messageTypeCode shouldBe "0200"
                result.requestCode.businessTypeCode shouldBe "400"
            }
        }
    }

    given("가상 계좌 '입금' 전문 연동 요청에 대한") {
        val messageTypeCode = "0200"
        val businessTypeCode = "600"

        `when`("'0200', '600' 전문 요청 코드와 일치한 Enum 클래스 생성하여") {
            val result = WooriBankMessageType.find(messageTypeCode, businessTypeCode)

            then("정상 확인한다") {
                result.requestCode.messageTypeCode shouldBe "0200"
                result.requestCode.businessTypeCode shouldBe "600"
            }
        }
    }

    given("가상 계좌 '입금 취소' 전문 연동 요청에 대한") {
        val messageTypeCode = "0420"
        val businessTypeCode = "700"

        `when`("'0200', '600' 전문 요청 코드와 일치한 Enum 클래스 생성하여") {
            val result = WooriBankMessageType.find(messageTypeCode, businessTypeCode)

            then("정상 확인한다") {
                result.requestCode.messageTypeCode shouldBe "0420"
                result.requestCode.businessTypeCode shouldBe "700"
            }
        }
    }

    given("가상 계좌 '입금 확인 통보' 전문 연동 요청에 대한") {
        val messageTypeCode = "0200"
        val businessTypeCode = "800"

        `when`("'0200', '800' 전문 요청 코드와 일치한 Enum 클래스 생성하여") {
            val result = WooriBankMessageType.find(messageTypeCode, businessTypeCode)

            then("정상 확인한다") {
                result.requestCode.messageTypeCode shouldBe "0200"
                result.requestCode.businessTypeCode shouldBe "800"
            }
        }
    }

})