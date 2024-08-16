package com.konai.vam.api.v1.sequencegenerator.service

import com.konai.vam.core.enumerate.SequenceGeneratorType

interface SequenceGeneratorAdapter {

    fun getNextSequence(type: SequenceGeneratorType, date: String, lockKey: String = "${type.name}:$date"): String

    fun getNextSequenceWithoutLock(type: SequenceGeneratorType, date: String): String

    fun deleteSequence(type: SequenceGeneratorType, date: String)

    fun findSequence(type: SequenceGeneratorType, date: String): String

}