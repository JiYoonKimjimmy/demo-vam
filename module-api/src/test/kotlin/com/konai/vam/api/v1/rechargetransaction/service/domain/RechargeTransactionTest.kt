package com.konai.vam.api.v1.rechargetransaction.service.domain

import com.konai.vam.api.testsupport.CustomBehaviorSpec
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.BaseException
import io.kotest.matchers.shouldBe

class RechargeTransactionTest : CustomBehaviorSpec({

    val rechargeTransactionFixture = rechargeTransactionFixture()

    given("충전 거래 요청 예외 발생하여") {
        val rechargeTransaction = rechargeTransactionFixture.make()

        `when`("'24_4000_154', '24_7000_154' 실패인 경우") {
            val detailMessage = "400 Bad Request: \"{\"reason\":\"24_4000_154\",\"message\":\"AUTHORIZATION - FAILED - V87_OVER_CREDIT_LIMIT\"}\""
            val result = rechargeTransaction.fail(BaseException(ErrorCode.UNKNOWN_ERROR, detailMessage))

            then("'RECHARGE_CARD_STATUS_IS_NOT_ACTIVE' 에러 코드 확인한다") {
                result.errorCode shouldBe ErrorCode.RECHARGE_CARD_STATUS_IS_NOT_ACTIVE
            }
        }

        `when`("'24_3000_334', '24_4000_510' 실패인 경우") {
            val detailMessage = "400 Bad Request: \"{\"reason\":\"24_3000_334\",\"message\":\"AUTHORIZATION - FAILED - V87_OVER_CREDIT_LIMIT\"}\""
            val result = rechargeTransaction.fail(BaseException(ErrorCode.UNKNOWN_ERROR, detailMessage))

            then("'RECHARGE_AMOUNT_EXCEEDED' 정보 정상 확인한다") {
                result.errorCode shouldBe ErrorCode.RECHARGE_AMOUNT_EXCEEDED
            }
        }

    }

})