package com.konai.vam.api.v1.sequencegenerator.service

import com.konai.vam.core.aop.distributedlock.DistributedLock
import com.konai.vam.core.common.ZERO_CHAR
import com.konai.vam.core.enumerate.SequenceGeneratorType
import com.konai.vam.core.repository.sequencegenerator.SequenceGeneratorEntityAdapter
import org.springframework.stereotype.Service

@Service
class SequenceGeneratorService(
    private val sequenceGeneratorEntityAdapter: SequenceGeneratorEntityAdapter
) : SequenceGeneratorAdapter {

    @DistributedLock(key = "#lockKey", waitTime = 10)
    override fun getNextSequence(type: SequenceGeneratorType, date: String, lockKey: String): String {
        val sequence = sequenceGeneratorEntityAdapter.getNextSequence(type, date)
        return sequence.value.toString().padStart(6, ZERO_CHAR)
    }

    override fun getNextSequenceWithoutLock(type: SequenceGeneratorType, date: String): String {
        val sequence = sequenceGeneratorEntityAdapter.getNextSequence(type, date)
        return sequence.value.toString().padStart(6, ZERO_CHAR)
    }

    override fun deleteSequence(type: SequenceGeneratorType, date: String) {
        sequenceGeneratorEntityAdapter.deleteSequence(type, date)
    }

    override fun findSequence(type: SequenceGeneratorType, date: String): String {
        return sequenceGeneratorEntityAdapter.findSequence(type, date).value.toString().padStart(6, ZERO_CHAR)
    }

}