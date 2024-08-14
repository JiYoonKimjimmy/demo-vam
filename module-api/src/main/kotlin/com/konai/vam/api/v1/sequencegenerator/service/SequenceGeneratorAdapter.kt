package com.konai.vam.api.v1.sequencegenerator.service

import com.konai.vam.core.enumerate.SequenceGeneratorType

interface SequenceGeneratorAdapter {

    fun getNextSequence(type: SequenceGeneratorType, date: String): String

}