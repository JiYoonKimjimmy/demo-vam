package com.konai.vam.api.v1.wooribank.service.management.domain

import com.konai.vam.api.testsupport.CustomBehaviorSpec
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.enumerate.WooriBankMessageType.*
import com.konai.vam.core.enumerate.WooriBankResponseCode
import fixtures.TestExtensionFunctions.generateUUID
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

class WooriBankManagementTest : CustomBehaviorSpec({

    val wooriBankManagementFixture = wooriBankManagementFixture()

    given("우리은행 가상 계좌 관리 전문 연동 요청되어") {
        val messageNo = generateUUID()

        `when`("전문 번호가 미정의된 요청인 경우") {
            val domain = wooriBankManagementFixture.make("9999", "999", messageNo)
            val result = shouldThrow<InternalServiceException> { domain.checkMessageTypeCode() }

            then("'K401 - 전문 FORMAT 오류' 예외 발생 확인한다") {
                result.errorCode shouldBe ErrorCode.WOORI_BANK_MESSAGE_CODE_INVALID

                val error = domain.fail(result)
                error.responseCode shouldBe WooriBankResponseCode.K401
                error.responseMessage shouldBe "Woori bank message code is invalid"
            }
        }

        `when`("가상 계좌 '조회' 전문 요청인 경우") {
            val domain = wooriBankManagementFixture.make("0200", "400", messageNo)
            val result = domain.checkMessageTypeCode()

            then("'VIRTUAL_ACCOUNT_INQUIRY(0200-400)' 우리은행 전문 구분 확인 성공한다") {
                result.messageTypeCode shouldBe VIRTUAL_ACCOUNT_INQUIRY.requestCode.messageTypeCode
                result.businessTypeCode shouldBe VIRTUAL_ACCOUNT_INQUIRY.requestCode.businessTypeCode
            }
        }

        `when`("가상 계좌 '입금' 전문 요청인 경우") {
            val domain = wooriBankManagementFixture.make("0200", "600", messageNo)
            val result = domain.checkMessageTypeCode()

            then("'VIRTUAL_ACCOUNT_DEPOSIT(0200-600)' 우리은행 전문 구분 확인 성공한다") {
                result.messageTypeCode shouldBe VIRTUAL_ACCOUNT_DEPOSIT.requestCode.messageTypeCode
                result.businessTypeCode shouldBe VIRTUAL_ACCOUNT_DEPOSIT.requestCode.businessTypeCode
            }
        }

        `when`("가상 계좌 '입금 취소' 전문 요청인 경우") {
            val domain = wooriBankManagementFixture.make("0420", "700", messageNo)
            val result = domain.checkMessageTypeCode()

            then("'VIRTUAL_ACCOUNT_DEPOSIT_CANCEL(0420-700)' 우리은행 전문 구분 확인 성공한다") {
                result.messageTypeCode shouldBe VIRTUAL_ACCOUNT_DEPOSIT_CANCEL.requestCode.messageTypeCode
                result.businessTypeCode shouldBe VIRTUAL_ACCOUNT_DEPOSIT_CANCEL.requestCode.businessTypeCode
            }
        }

        `when`("가상 계좌 '입금 확인 통보' 전문 요청인 경우") {
            val domain = wooriBankManagementFixture.make("0200", "800", messageNo)
            val result = domain.checkMessageTypeCode()

            then("'VIRTUAL_ACCOUNT_DEPOSIT_CONFIRM(0420-800)' 우리은행 전문 구분 확인 성공한다") {
                result.messageTypeCode shouldBe VIRTUAL_ACCOUNT_DEPOSIT_CONFIRM.requestCode.messageTypeCode
                result.businessTypeCode shouldBe VIRTUAL_ACCOUNT_DEPOSIT_CONFIRM.requestCode.businessTypeCode
            }
        }
    }

})