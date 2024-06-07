package com.konai.vam.api.v1.virtualaccount.service

import io.kotest.core.spec.style.BehaviorSpec

class VirtualAccountServiceTest : BehaviorSpec({

    given("가상 계좌 등록 요청하면") {

        `when`("이미 동일한 계좌번호 & 은행코드 정보 등록된 경우") {

            then("중복 가상 계좌 등록 예외 발생하여 실패한다") {

            }
        }

        `when`("등록 가능한 가상 계좌 정보인 경우") {

            then("저장 성공한다") {

            }
        }
    }

})