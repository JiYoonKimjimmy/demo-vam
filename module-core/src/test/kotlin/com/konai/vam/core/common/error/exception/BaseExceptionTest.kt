package com.konai.vam.core.common.error.exception

import com.konai.vam.core.common.error.ErrorCode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class BaseExceptionTest : StringSpec({

    "외부 연동 API 에러 메시지 변환하여 'reason' & 'message' 추출 확인한다" {
        // given
        val detailMessage = "400 Bad Request: \"{\"par\":\"Q1E39E5C50F1DE90B6F502C607F\",\"reason\":\"24_4000_510\",\"message\":\"AUTHORIZATION - FAILED - V87_OVER_CREDIT_LIMIT\",\"transactionId\":\"20240809125859002914\",\"rechargerId\":\"4101098153365CX\",\"authYn\":\"X\",\"nrNumber\":null,\"amount\":200000,\"success\":false}\""

        // when
        val message = BaseException(ErrorCode.UNKNOWN_ERROR, detailMessage).parseDetailMessage()

        // then
        message?.first shouldBe "24_4000_510"
        message?.second shouldBe "AUTHORIZATION - FAILED - V87_OVER_CREDIT_LIMIT"
    }

})