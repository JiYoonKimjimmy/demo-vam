package com.konai.vam.core.repository.sequencegenerator

import com.konai.vam.core.testsupport.CustomDataJpaTest
import com.konai.vam.core.enumerate.SequenceGeneratorType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired

@CustomDataJpaTest
class SequenceGeneratorRepositoryTest @Autowired constructor(
    private val sequenceGeneratorJpaRepository: SequenceGeneratorJpaRepository
) : BehaviorSpec({

    val sequenceGeneratorRepository = SequenceGeneratorRepository(sequenceGeneratorJpaRepository)

    given("'20240813' 일자 기준 새로운 Sequence 요청하여") {
        val date = "20240813"
        val type = SequenceGeneratorType.WR_BANK

        `when`("요청 일자의 Sequence 없는 경우") {
            val result = sequenceGeneratorRepository.getNextSequence(type, date)

            then("새로운 Entity 생성하고, 증가한 값 정상 확인한다") {
                result shouldNotBe null
                result.value shouldBe 1
            }

            then("DB 저장 정보 조회 정상 확인한다") {
                val entity = sequenceGeneratorJpaRepository.findByTypeAndDate(type, date).get()
                entity shouldNotBe null
                entity.value shouldBe 1
            }
        }
    }

})