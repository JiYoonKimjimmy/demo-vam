package com.konai.vam.api.v1.wooribank.service.common

import com.konai.vam.core.common.model.wooribank.WooriBankCommonMessage
import com.konai.vam.core.enumerate.WooriBankMessageType.Code

interface WooriBankCommonMessageAdapter {

    fun generateCommonMessage(messageCode: Code): WooriBankCommonMessage

}