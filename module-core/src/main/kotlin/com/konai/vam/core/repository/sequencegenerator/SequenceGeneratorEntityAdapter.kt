package com.konai.vam.core.repository.sequencegenerator

import com.konai.vam.core.enumerate.SequenceGeneratorType
import com.konai.vam.core.repository.sequencegenerator.entity.SequenceGeneratorEntity

interface SequenceGeneratorEntityAdapter {

    fun getNextSequence(type: SequenceGeneratorType, date: String): SequenceGeneratorEntity

    fun findSequence(type: SequenceGeneratorType, date: String): SequenceGeneratorEntity

    fun deleteSequence(type: SequenceGeneratorType, date: String)

}