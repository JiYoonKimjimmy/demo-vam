package com.konai.vam.api.v1.wooribank.service.work

import com.konai.vam.core.common.model.wooribank.WooriBankCommonMessage
import com.konai.vam.core.enumerate.WooriBankMessage

interface WooriBankWorkAdapter {

    fun work(message: WooriBankMessage): WooriBankCommonMessage

}