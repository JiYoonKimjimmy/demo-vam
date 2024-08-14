package com.konai.vam.api.v1.sequencegenerator.service

import com.konai.vam.core.common.ZERO_CHAR
import com.konai.vam.core.enumerate.SequenceGeneratorType
import com.konai.vam.core.repository.sequencegenerator.SequenceGeneratorEntityAdapter
import org.springframework.stereotype.Service

@Service
class SequenceGeneratorService(
    private val sequenceGeneratorEntityAdapter: SequenceGeneratorEntityAdapter
) : SequenceGeneratorAdapter {

    override fun getNextSequence(type: SequenceGeneratorType, date: String): String {
        val sequence = sequenceGeneratorEntityAdapter.getNextSequence(type, date)
        return sequence.value.toString().padStart(6, ZERO_CHAR)
    }

}