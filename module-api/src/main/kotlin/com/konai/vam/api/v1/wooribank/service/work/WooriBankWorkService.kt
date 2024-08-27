package com.konai.vam.api.v1.wooribank.service.work

import com.konai.vam.api.v1.wooribank.service.common.WooriBankCommonMessageAdapter
import com.konai.vam.core.enumerate.WooriBankMessage
import com.konai.vam.core.restclient.wooribank.PostWooriWorkRequest
import com.konai.vam.core.restclient.wooribank.PostWooriWorkResponse
import com.konai.vam.core.restclient.wooribank.WooriBankRestClient
import org.springframework.stereotype.Service

@Service
class WooriBankWorkService(
    private val wooriBankCommonMessageAdapter: WooriBankCommonMessageAdapter,
    private val wooriBankRestClient: WooriBankRestClient,
) : WooriBankWorkAdapter {

    override fun work(message: WooriBankMessage): PostWooriWorkResponse {
        return PostWooriWorkRequest(message = wooriBankCommonMessageAdapter.generateCommonMessage(message.requestCode))
            .let { wooriBankRestClient.postWooriBankWork(it) }
    }

}