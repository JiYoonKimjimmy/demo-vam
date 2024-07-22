package com.konai.vam.api.v1.rechargetransaction.service

import com.konai.vam.api.v1.kotestspec.KoTestBehaviorSpec
import com.konai.vam.core.enumerate.Result.FAILED
import com.konai.vam.core.enumerate.Result.SUCCESS
import io.kotest.matchers.shouldBe

class RechargeTransactionSaveServiceTest : KoTestBehaviorSpec({

    val rechargeTransactionFixture = rechargeTransactionFixture()
    val rechargeTransactionSaveService = rechargeTransactionSaveService()

    given("충전 내역 완료 처리되면") {
        `when`("결과가 '성공' 인 경우") {
            val domain = rechargeTransactionFixture.make(result = SUCCESS)
            val result = rechargeTransactionSaveService.save(domain)

            then("'SUCCESSED' reault 충전 내역 Entity 정보 저장 성공한다") {
                result.result shouldBe SUCCESS
            }
        }

        `when`("결과가 '실패' 인 경우") {
            val domain = rechargeTransactionFixture.make(result = FAILED)
            val result = rechargeTransactionSaveService.save(domain)

            then("'FAILED' reault 충전 내역 Entity 정보 저장 성공한다") {
                result.result shouldBe FAILED
            }
        }
    }

})