package com.konai.vam.api.v1.virtualaccountbank.service

import com.konai.vam.api.v1.kotestspec.KoTestBehaviorSpec
import com.konai.vam.core.repository.virtualaccountbank.VirtualAccountBankConst
import com.konai.vam.core.repository.virtualaccountbank.jdsl.VirtualAccountBankPredicate
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class VirtualAccountBankFindServiceTest : KoTestBehaviorSpec({

    val virtualAccountBankEntityAdaptor = virtualAccountBankEntityAdaptor()
    val virtualAccountBankFindService = virtualAccountBankFindService()

    given("'020' 은행 코드 기준 가상 계좌 은행 정보 조회 요청하여") {
        virtualAccountBankEntityAdaptor.save(VirtualAccountBankConst.woori)
        val bankCode = VirtualAccountBankConst.woori.bankCode

        `when`("정상 조회 성공하여") {
            val result = virtualAccountBankFindService.findByBankCode(bankCode)

            then("가상 계좌 은행 정보 확인한다") {
                result shouldNotBe null
                result.bankCode shouldBe bankCode
                result.bankName shouldBe "우리은행"
            }
        }
    }

    given("'020' 은행 코드 기준 가상 계좌 은행 정보 전체 조회 요청하여") {
        virtualAccountBankEntityAdaptor.save(VirtualAccountBankConst.woori)
        val predicate = VirtualAccountBankPredicate(bankCode = VirtualAccountBankConst.woori.bankCode)

        `when`("1건 정상 조회 성공하여") {
            val result = virtualAccountBankFindService.findAllByPredicate(predicate)

            then("가상 계좌 은행 전체 정보 확인한다") {
                result shouldNotBe null
                result.pageable.totalElements shouldBe 1
                result.content.first().bankCode shouldBe VirtualAccountBankConst.woori.bankCode
            }
        }
    }

})