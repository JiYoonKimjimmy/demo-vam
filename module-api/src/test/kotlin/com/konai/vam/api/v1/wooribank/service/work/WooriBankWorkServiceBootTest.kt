package com.konai.vam.api.v1.wooribank.service.work

import com.konai.vam.api.v1.kotestspec.CustomBehaviorSpec
import com.konai.vam.core.enumerate.SequenceGeneratorType
import com.konai.vam.core.enumerate.WooriBankMessage
import com.konai.vam.core.repository.sequencegenerator.SequenceGeneratorEntityAdapter
import com.konai.vam.core.util.convertPatternOf
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class WooriBankWorkServiceBootTest @Autowired constructor(
    private val wooriBankWorkService: WooriBankWorkService,
    private val sequenceGeneratorEntityAdapter: SequenceGeneratorEntityAdapter
) : CustomBehaviorSpec({

    xgiven("우리은행 '업무 개시' 전문 연동 요청하여") {
        val message = WooriBankMessage.WORK_START
        val entity = sequenceGeneratorEntityAdapter.findSequence(SequenceGeneratorType.WR_BANK, LocalDate.now().convertPatternOf())

        `when`("생성 전문 성공인 경우") {
            val result = wooriBankWorkService.work(message)

            then("'000001' 전문번호 요청 정상 확인한다") {
                result.messageNo.toInt() shouldBeGreaterThan 1
                result.messageNo.toInt() shouldBe entity.value + 1
            }
        }
    }

})