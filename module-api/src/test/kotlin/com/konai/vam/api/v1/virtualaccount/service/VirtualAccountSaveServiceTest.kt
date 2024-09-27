package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.testsupport.CustomBehaviorSpec
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class VirtualAccountSaveServiceTest : CustomBehaviorSpec({

    val virtualAccountFixture = virtualAccountFixture()
    val virtualAccountSaveService = virtualAccountSaveService()

    given("가상계좌 정보 저장 요청되어") {
        val domain = virtualAccountFixture.make()

        `when`("신규 정보 저장 요청인 경우") {
            val result = virtualAccountSaveService.save(domain)

            then("정상 확인한다") {
                result shouldNotBe null
                result.id shouldNotBe null
            }
        }

        `when`("'accountNo' & 'bankCode' 중복 저장 요청인 경우") {
            val exception = shouldThrow<InternalServiceException> { virtualAccountSaveService.save(domain) }

            then("'VIRTUAL_ACCOUNT_IS_DUPLICATED' 에러 정상 확인한다") {
                exception.errorCode shouldBe ErrorCode.VIRTUAL_ACCOUNT_IS_DUPLICATED
            }
        }
    }

})