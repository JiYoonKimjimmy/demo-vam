package com.konai.vam.api.v1.wooribank.service.message

import com.konai.vam.api.v1.sequencegenerator.service.SequenceGeneratorAdapter
import com.konai.vam.core.common.ZERO_CHAR
import com.konai.vam.core.common.model.wooribank.WooriBankMessage
import com.konai.vam.core.enumerate.SequenceGeneratorType
import com.konai.vam.core.enumerate.WooriBankMessageType.Code
import com.konai.vam.core.util.convertPatternOf
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class WooriBankMessageGenerateService(
    private val sequenceGeneratorAdapter: SequenceGeneratorAdapter
) : WooriBankMessageGenerateAdapter {

    override fun generateMessage(messageCode: Code): WooriBankMessage {
        return WooriBankMessage(
            messageTypeCode = messageCode.messageTypeCode,
            businessTypeCode = messageCode.businessTypeCode,
            messageNo = getMessageNo())
    }

    private fun getMessageNo(): String {
        return sequenceGeneratorAdapter.getNextSequence(SequenceGeneratorType.WR_BANK, LocalDate.now().convertPatternOf())
            .toString().padStart(6, ZERO_CHAR)
    }

}