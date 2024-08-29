package com.konai.vam.api.v1.wooribank.service.work

import com.konai.vam.api.v1.kotestspec.CustomBehaviorSpec
import com.konai.vam.core.enumerate.WooriBankMessage
import io.kotest.matchers.shouldBe
import io.mockk.every

class WooriBankWorkServiceTest : CustomBehaviorSpec({

    val wooriBankWorkService = wooriBankWorkService()
    val mockWooriBankRestClient = mockWooriBankRestClient()
    val wooriBankCommonMessageFixture = wooriBankCommonMessageFixture()

    given("우리은행 '업무 개시' 전문 연동 요청하여") {
        val message = WooriBankMessage.WORK_START

        val wooriBankCommonMessage = wooriBankCommonMessageFixture.make(message.requestCode, "000001")
        every { mockWooriBankRestClient.postWooriBankWork(any()) } returns wooriBankCommonMessage

        `when`("생성 전문 성공인 경우") {
            val result = wooriBankWorkService.work(message)

            then("'000001' 전문번호 요청 정상 확인한다") {
                result.messageNo shouldBe "000001"
            }
        }
    }

})