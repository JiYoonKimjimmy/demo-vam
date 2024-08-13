package com.konai.vam.api.v1.sequencegenerator.service

import com.konai.vam.core.common.ZERO_CHAR
import com.konai.vam.core.enumerate.SequenceGeneratorType
import com.konai.vam.core.repository.sequencegenerator.SequenceGeneratorEntityAdapter
import com.konai.vam.core.util.convertPatternOf
import org.springframework.stereotype.Service
import sun.jvm.hotspot.oops.CellTypeState.value
import java.time.LocalDate

@Service
class SequenceGeneratorService(
    private val sequenceGeneratorEntityAdapter: SequenceGeneratorEntityAdapter
) : SequenceGeneratorAdapter {

    override fun getNextSequence(date: String, type: SequenceGeneratorType): String {
        val sequence = sequenceGeneratorEntityAdapter.getNextSequence(date, type)
        return sequence.value.toString().padStart(6, ZERO_CHAR)
    }

}