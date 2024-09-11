package com.konai.vam.api.v1.wooribank.controller.model

import com.konai.vam.core.enumerate.WooriBankResponseCode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class WooriBankManagementModelMapperTest : StringSpec({

    "우리은행 empty string 응답코드 enum 변환 테스트 정상 확인한다" {
        // given
        val responseCode = ""

        // when
        val result = responseCode.takeIf { it.isNotBlank() }?.let(WooriBankResponseCode::valueOf)

        // then
        result shouldBe null
    }

})