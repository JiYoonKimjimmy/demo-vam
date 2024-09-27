package com.konai.vam.core.repository.parentaccount

import com.konai.vam.core.testsupport.CustomDataJpaTest
import com.konai.vam.core.common.getContentFirstOrNull
import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import fixtures.TestExtensionFunctions.generateUUID
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.data.domain.PageRequest
import java.util.*

@CustomDataJpaTest
class ParentAccountJpaRepositoryTest(
    private val parentAccountJpaRepository: ParentAccountJpaRepository
) : StringSpec({

    lateinit var saved: ParentAccountEntity

    beforeSpec {
        val parentAccountNo = generateUUID()
        val bankCode = "020"
        val entity = ParentAccountEntity(parentAccountNo = parentAccountNo, bankCode = bankCode)
        saved = parentAccountJpaRepository.save(entity)
    }

    "모계좌 Entity 정보 신규 생성하여 정상 확인한다" {
        // given
        val parentAccountNo = generateUUID()
        val bankCode = "020"
        val entity = ParentAccountEntity(parentAccountNo = parentAccountNo, bankCode = bankCode)

        // when
        val result = parentAccountJpaRepository.save(entity)

        // then
        result.id shouldNotBe null
        result.id!! shouldBeGreaterThan 0
        result.parentAccountNo shouldBe parentAccountNo
    }

    "모계좌 Entity 정보 'parentAccountNo' 조건으로 조회하여 정상 확인한다" {
        // given
        val parentAccountNo = saved.parentAccountNo

        // when
        val result = parentAccountJpaRepository.findByParentAccountNo(parentAccountNo)

        // then
        result shouldNotBe null
        result!!.id shouldBe saved.id
    }

    "모계좌 Entity 정보 'parentAccountNo', 'bankCode' 조건 존재 여부 정상 확인한다" {
        // given
        val parentAccountNo = saved.parentAccountNo
        val bankCode = "020"

        // when
        val result = parentAccountJpaRepository.existsByParentAccountNoAndBankCode(parentAccountNo, bankCode)

        // then
        result shouldBe true
    }

    "모계좌 Entity 정보 'parentAccountNo', 'bankCode' 조건으로 조회하여 정상 확인한다" {
        // given
        val parentAccountNo = saved.parentAccountNo
        val bankCode = "020"
        val predicate = ParentAccountPredicate(parentAccountNo = parentAccountNo, bankCode = bankCode)

        // when
        val result = parentAccountJpaRepository.findSlice(
            pageable = PageRequest.of(0, 1),
            init = predicate.generateQuery()
        )
            .getContentFirstOrNull()
            .let { Optional.ofNullable(it) }
            .orElse(null)

        // then
        result.id shouldBe saved.id
    }

})