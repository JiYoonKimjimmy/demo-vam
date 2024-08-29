package com.konai.vam.api.v1.sequencegenerator.service

import com.konai.vam.core.enumerate.SequenceGeneratorType

interface SequenceGeneratorAdapter {

    fun getNextSequence(type: SequenceGeneratorType, date: String, lockKey: String = "${type.name}:$date"): Long

    fun getNextSequenceWithoutLock(type: SequenceGeneratorType, date: String): Long

    fun findSequence(type: SequenceGeneratorType, date: String): Long

    fun deleteSequence(type: SequenceGeneratorType, date: String)

}