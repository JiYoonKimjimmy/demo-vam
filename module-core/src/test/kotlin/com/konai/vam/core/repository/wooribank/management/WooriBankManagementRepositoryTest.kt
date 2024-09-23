package com.konai.vam.core.repository.wooribank.management

import com.konai.vam.core.common.annotation.CustomDataJpaTest
import com.konai.vam.core.enumerate.WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT
import com.konai.vam.core.enumerate.WooriBankResponseCode.`0000`
import com.konai.vam.core.repository.wooribank.management.jdsl.WooriBankManagementPredicate
import fixtures.WooriBankManagementEntityFixture
import fixtures.generateUUID
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired

@CustomDataJpaTest
class WooriBankManagementRepositoryTest(
    @Autowired
    private val wooriBankManagementJpaRepository: WooriBankManagementJpaRepository
) : BehaviorSpec({

    val wooriBankManagementRepository = WooriBankManagementRepository(wooriBankManagementJpaRepository)
    val wooriBankManagementEntityFixture = WooriBankManagementEntityFixture()

    given("우리은행 전문 연동 'messageTypeCode' & 'businessTypeCode' & 'messageNo' & 'responseCode' 기준 조회 요청하여") {
        val messageNo = generateUUID(6)
        val messageCode = VIRTUAL_ACCOUNT_DEPOSIT.requestCode
        val responseCode = `0000`

        val predicate = WooriBankManagementPredicate(
            messageTypeCode = messageCode.messageTypeCode,
            businessTypeCode = messageCode.businessTypeCode,
            messageNo = messageNo,
            responseCode = responseCode
        )

        `when`("동일한 정보가 없는 경우") {
            val result = wooriBankManagementRepository.findByPredicate(predicate).orElse(null)

            then("조회 결과 'null' 정상 확인한다") {
                result shouldBe null
            }
        }

        // 우리은행 전문 연동 내역 저장
        wooriBankManagementJpaRepository.save(wooriBankManagementEntityFixture.make(messageCode = messageCode, messageNo = messageNo, responseCode = responseCode))

        `when`("동일한 정보가 있는 경우") {

            val result = wooriBankManagementRepository.findByPredicate(predicate).orElse(null)

            then("조회 결과 정상 확인한다") {
                result shouldNotBe null
                result.messageNo shouldBe messageNo
            }
        }

    }

})