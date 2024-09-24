package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.api.v1.kotestspec.CustomBehaviorSpec
import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import fixtures.TestExtensionFunctions.generateUUID
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ParentAccountManagementServiceTest : CustomBehaviorSpec({

    val parentAccountManagementService = parentAccountManagementService()
    val parentAccountEntityAdapter = parentAccountEntityAdapter()

    given("모계좌 등록 요청되어") {
        val parentAccountNo = generateUUID()
        val bankCode = "020"
        val domain = ParentAccount(parentAccountNo = parentAccountNo, bankCode = bankCode)

        `when`("신규 생성 요청인 경우") {
            val result = parentAccountManagementService.save(domain)

            then("생성 결과 정상 확인한다") {
                result.id shouldNotBe null
            }

            then("DB 정보 '1'건 생성 정상 확인한다") {
                val entities = parentAccountEntityAdapter.findAll()
                entities.size shouldBe 1
                entities.first().parentAccountNo shouldBe parentAccountNo
            }
        }

        `when`("'parentAccountNo' & 'bankCode' 조건 동일한 모계좌 정보 있는 경우") {
            val exception = shouldThrow<InternalServiceException> { parentAccountManagementService.save(domain) }

            then("'PARENT_ACCOUNT_NO_IS_DUPLICATED' 예외 발생 확인한다") {
                exception.errorCode shouldBe ErrorCode.PARENT_ACCOUNT_NO_IS_DUPLICATED
            }
        }
    }

})