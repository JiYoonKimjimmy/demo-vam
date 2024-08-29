package com.konai.vam.api.v1.wooribank.service.work

import com.konai.vam.api.v1.wooribank.service.message.WooriBankMessageGenerateAdapter
import com.konai.vam.core.common.model.wooribank.WooriBankCommonMessage
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.restclient.wooribank.PostWooriWorkRequest
import com.konai.vam.core.restclient.wooribank.WooriBankRestClient
import org.springframework.stereotype.Service

@Service
class WooriBankWorkService(
    private val wooriBankMessageGenerateAdapter: WooriBankMessageGenerateAdapter,
    private val wooriBankRestClient: WooriBankRestClient,
) : WooriBankWorkAdapter {

    override fun work(message: WooriBankMessageType): WooriBankCommonMessage {
        return PostWooriWorkRequest(message = wooriBankMessageGenerateAdapter.generateMessage(message.requestCode))
            .let { wooriBankRestClient.postWooriBankWork(it) }
    }

}