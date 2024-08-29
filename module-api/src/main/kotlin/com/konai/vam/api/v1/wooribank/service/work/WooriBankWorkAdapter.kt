package com.konai.vam.api.v1.wooribank.service.work

import com.konai.vam.core.common.model.wooribank.WooriBankMessage
import com.konai.vam.core.enumerate.WooriBankMessageType

interface WooriBankWorkAdapter {

    fun work(message: WooriBankMessageType): WooriBankMessage

}