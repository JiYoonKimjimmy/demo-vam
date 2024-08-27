package com.konai.vam.api.v1.wooribank.service.work

import com.konai.vam.core.enumerate.WooriBankMessage
import com.konai.vam.core.restclient.wooribank.PostWooriWorkResponse

interface WooriBankWorkAdapter {

    fun work(message: WooriBankMessage): PostWooriWorkResponse

}