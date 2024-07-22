package com.konai.vam.api.v1.wooribank.service.work

import com.konai.vam.api.v1.kotestspec.KoTestBehaviorSpec
import com.konai.vam.core.common.model.wooribank.WooriBankCommonModel
import com.konai.vam.core.enumerate.WooriBankMessage
import com.konai.vam.core.restclient.wooribank.PostWooriWorkRequest
import io.kotest.matchers.shouldBe

class WooriBankWorkSerivceTest : KoTestBehaviorSpec({

    given("우리은행 '업무 개시' 전문 요청하여") {
        val message = WooriBankMessage.WORK_START
        `when`("정상 전문 요청인 경우") {
            val request = PostWooriWorkRequest(WooriBankCommonModel(message.requestCode))
            then("전문코드 '0800', 업무구분 '100' 요청 정보 생성 확인한다") {
                request.model.messageTypeCode shouldBe "0800"
                request.model.businessTypeCode shouldBe "100"
            }
        }
    }

})