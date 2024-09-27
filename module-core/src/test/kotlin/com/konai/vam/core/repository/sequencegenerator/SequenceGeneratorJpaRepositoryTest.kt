package com.konai.vam.core.repository.sequencegenerator

import com.konai.vam.core.testsupport.CustomDataJpaTest
import com.konai.vam.core.enumerate.SequenceGeneratorType
import fixtures.SequenceGeneratorEntityFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

@CustomDataJpaTest
class SequenceGeneratorJpaRepositoryTest @Autowired constructor(
    private val sequenceGeneratorJpaRepository: SequenceGeneratorJpaRepository
): BehaviorSpec({

    val sequenceGeneratorEntityFixture = SequenceGeneratorEntityFixture()

    given("'20240813' 일자 'WOORI_BANK' 구분의 Sequence 채번 요청하여") {
        val date = "20240813"
        val type = SequenceGeneratorType.WR_BANK

        `when`("DB 정보 없는 경우") {
            val result = sequenceGeneratorJpaRepository.findByTypeAndDate(type, date)
            
            then("'null' 반환 정상 확인한다") {
                result.isPresent shouldBe false
            }
        }

        // Sequence 생성
        sequenceGeneratorJpaRepository.save(sequenceGeneratorEntityFixture.make(type = type, date = date))

        `when`("DB 정보 있는 경우") {
            val result = sequenceGeneratorJpaRepository.findByTypeAndDate(type, date)

            then("조회 결과 정상 확인한다") {
                result.isPresent shouldBe true
                val entity = result.get()
                entity.date shouldBe date
                entity.type shouldBe type
                entity.value shouldBe 0
            }
        }
    }
    
})