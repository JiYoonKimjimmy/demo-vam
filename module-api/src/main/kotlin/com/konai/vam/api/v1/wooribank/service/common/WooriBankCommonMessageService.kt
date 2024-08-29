package com.konai.vam.api.v1.wooribank.service.common

import com.konai.vam.api.v1.sequencegenerator.service.SequenceGeneratorAdapter
import com.konai.vam.core.common.model.wooribank.WooriBankCommonMessage
import com.konai.vam.core.enumerate.SequenceGeneratorType
import com.konai.vam.core.enumerate.WooriBankMessageType.Code
import com.konai.vam.core.util.convertPatternOf
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class WooriBankCommonMessageService(
    private val sequenceGeneratorAdapter: SequenceGeneratorAdapter
) : WooriBankCommonMessageAdapter {

    override fun generateCommonMessage(messageCode: Code): WooriBankCommonMessage {
        val sequence = sequenceGeneratorAdapter.getNextSequence(SequenceGeneratorType.WR_BANK, LocalDate.now().convertPatternOf())
        return WooriBankCommonMessage(messageCode, sequence)
    }

}