package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.api.v1.kotestspec.CustomBehaviorSpec
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import fixtures.TestExtensionFunctions.generateUUID
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize

class ParentAccountFindServiceTest : CustomBehaviorSpec({

    val parentAccountFindService = parentAccountFindService()
    val parentAccountEntityAdapter = parentAccountEntityAdapter()
    val parentAccountEntityFixture = parentAccountEntityFixture()

    val saveParentEntityProc: (String, String) -> (Unit) = { parentAccountNo, bankCode ->
        val entity = parentAccountEntityFixture.make(parentAccountNo = parentAccountNo, bankCode = bankCode)
        parentAccountEntityAdapter.save(entity)
    }

    given("모계좌 정보 다건 조회 요청되어") {

        `when`("등록된 정보 없는 경우") {
            val predicate = ParentAccountPredicate()
            val result = parentAccountFindService.findAll(predicate)

            then("Empty 목록 반환 정상 확인한다") {
                result.content.shouldBeEmpty()
            }
        }

        val parentAccountNo = generateUUID()
        val bankCode = "020"
        saveParentEntityProc(parentAccountNo, bankCode)

        `when`("'parentAccountNo' 조건 일치한 정보 있는 경우") {
            val predicate = ParentAccountPredicate(parentAccountNo = parentAccountNo)
            val result = parentAccountFindService.findAll(predicate)

            then("'1건' 조회 결과 정상 확인한다") {
                result.content shouldHaveSize 1
            }
        }

        `when`("'parentAccountNo', 'bankCode' 조건 일치한 정보 없는 경우") {
            val predicate = ParentAccountPredicate(parentAccountNo = parentAccountNo, bankCode = "010")
            val result = parentAccountFindService.findAll(predicate)

            then("Empty 목록 반환 정상 확인한다") {
                result.content.shouldBeEmpty()
            }
        }

        `when`("'parentAccountNo', 'bankCode' 조건 일치한 정보 있는 경우") {
            val predicate = ParentAccountPredicate(parentAccountNo = parentAccountNo, bankCode = bankCode)
            val result = parentAccountFindService.findAll(predicate)

            then("'1건' 조회 결과 정상 확인한다") {
                result.content shouldHaveSize 1
            }
        }

        saveParentEntityProc(generateUUID(), bankCode)

        `when`("'bankCode' 조건 일치한 정보 있는 경우") {
            val predicate = ParentAccountPredicate(bankCode = bankCode)
            val result = parentAccountFindService.findAll(predicate)

            then("'2건' 조회 결과 정상 확인한다") {
                result.content shouldHaveSize 2
            }
        }

    }

})