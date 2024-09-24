package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.kotestspec.CustomBehaviorSpec
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import fixtures.TestExtensionFunctions.generateUUID
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class VirtualAccountServiceTest : CustomBehaviorSpec({

    val virtualAccountService = virtualAccountService()

    val virtualAccountEntityAdapter = virtualAccountEntityAdapter()
    val virtualAccountEntityFixture = virtualAccountEntityFixture()

    afterTest {
        virtualAccountEntityAdapter.clear()
    }

    given("'accountNo' 요청 정보 기준 가상 계좌 단건 조회 요청하여") {
        val accountNo = generateUUID()
        val par = generateUUID()
        val predicate = VirtualAccountPredicate(accountNo = accountNo)

        `when`("조회 결과 없는 경우") {
            val exception = shouldThrow<ResourceNotFoundException> { virtualAccountService.findOne(predicate) }

            then("ResourceNotFoundException 예외 발생 정상 확인한다") {
                exception.errorCode shouldBe ErrorCode.VIRTUAL_ACCOUNT_NOT_FOUND
            }
        }

        virtualAccountEntityAdapter.save(virtualAccountEntityFixture.make(accountNo = accountNo, par = par))

        `when`("조회 결과 있는 경우") {
            val result = virtualAccountService.findOne(predicate)

            then("가상 계좌 조회 결과 정상 확인한다") {
                result shouldNotBe null
                result.bankAccount.accountNo shouldBe accountNo
                result.par shouldBe par
            }
        }
    }

    given("가상 계좌 다건 조회 요청하여") {
        val number = 0
        val size = 1
        val predicate = VirtualAccountPredicate()
        val pageableRequest = PageableRequest(number, size)

        `when`("조회 결과 없는 경우") {
            val result = virtualAccountService.findPage(predicate, pageableRequest)

            then("0건 조회 성공한다") {
                result.content.shouldBeEmpty()
            }
        }

        virtualAccountEntityAdapter.save(virtualAccountEntityFixture.make())

        `when`("size 1건 요청하는 경우") {
            val result = virtualAccountService.findPage(predicate, pageableRequest)

            then("1건 조회 성공한다") {
                result.content.size shouldBe 1
            }
        }
    }

})