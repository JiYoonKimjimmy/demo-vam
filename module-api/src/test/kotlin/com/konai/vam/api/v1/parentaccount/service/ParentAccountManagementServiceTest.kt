package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.api.testsupport.CustomBehaviorSpec
import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import fixtures.TestExtensionFunctions.generateSequence
import fixtures.TestExtensionFunctions.generateUUID
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ParentAccountManagementServiceTest : CustomBehaviorSpec({

    val parentAccountManagementService = parentAccountManagementService()
    val parentAccountEntityAdapter = parentAccountEntityAdapter()
    val parentAccountEntityFixture = parentAccountEntityFixture()

    given("모계좌 등록 요청되어") {
        val parentAccountNo = generateUUID()
        val bankCode = "123"
        val domain = ParentAccount(parentAccountNo = parentAccountNo, bankCode = bankCode)

        `when`("신규 생성 요청인 경우") {
            val result = parentAccountManagementService.create(domain)

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
            val exception = shouldThrow<InternalServiceException> { parentAccountManagementService.create(domain) }

            then("'PARENT_ACCOUNT_NO_IS_DUPLICATED' 예외 발생 정상 확인한다") {
                exception.errorCode shouldBe ErrorCode.PARENT_ACCOUNT_IS_DUPLICATED
            }
        }
    }

    given("모계좌 수정 요청되어") {
        val id = generateSequence()
        val parentAccountNo = generateUUID()
        val bankCode = "123"

        `when`("'id' 기준 등록 정보 없는 경우") {
            val domain = ParentAccount(id = id, parentAccountNo = parentAccountNo, bankCode = bankCode)
            val exception = shouldThrow<ResourceNotFoundException> { parentAccountManagementService.update(domain) }

            then("'PARENT_ACCOUNT_NOT_FOUND' 예외 발생 정상 확인한다") {
                exception.errorCode shouldBe ErrorCode.PARENT_ACCOUNT_NOT_FOUND
            }
        }

        parentAccountEntityAdapter.save(parentAccountEntityFixture.make(id, parentAccountNo, bankCode))

        `when`("'parentAccountNo' & 'bankCode' 중복 등록인 경우") {
            val domain = ParentAccount(id = id, parentAccountNo = parentAccountNo, bankCode = bankCode)
            val exception = shouldThrow<InternalServiceException> { parentAccountManagementService.update(domain) }

            then("'PARENT_ACCOUNT_IS_DUPLICATED' 예외 발생 확인한다") {
                exception.errorCode shouldBe ErrorCode.PARENT_ACCOUNT_IS_DUPLICATED
            }
        }

        `when`("정상 수정 요청인 경우") {
            val domain = ParentAccount(id = id, parentAccountNo = "1234567890", bankCode = bankCode)
            val result = parentAccountManagementService.update(domain)

            then("수정 성공 정상 확인한다") {
                result.id shouldBe id
                result.parentAccountNo shouldBe "1234567890"
                result.bankCode shouldBe bankCode
            }
        }
    }

    given("모계좌 삭제 요청되어") {
        val id = generateSequence()
        val parentAccountNo = generateUUID()
        val bankCode = "123"

        `when`("'id' 기준 등록 정보 없는 경우") {
            val exception = shouldThrow<ResourceNotFoundException> { parentAccountManagementService.delete(id) }

            then("'PARENT_ACCOUNT_NOT_FOUND' 예외 발생 정상 확인한다") {
                exception.errorCode shouldBe ErrorCode.PARENT_ACCOUNT_NOT_FOUND
            }
        }

        parentAccountEntityAdapter.save(parentAccountEntityFixture.make(id, parentAccountNo, bankCode))

        `when`("정상 삭제 요청인 경우") {
            parentAccountManagementService.delete(id)

            then("삭제 성공 정상 확인한다") {
                val result = parentAccountEntityAdapter.findByPredicate(ParentAccountPredicate(id))
                result shouldBe null
            }
        }
    }

})