package com.konai.vam.api.v1.wooribank.service.message

import com.konai.vam.core.common.model.wooribank.WooriBankMessage
import com.konai.vam.core.enumerate.WooriBankMessageType.Code

interface WooriBankMessageGenerateAdapter {

    fun generateMessage(messageCode: Code): WooriBankMessage

}