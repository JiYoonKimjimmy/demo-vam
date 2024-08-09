package com.konai.vam.api.v1.wooribank.service.work

import com.konai.vam.core.common.model.wooribank.WooriBankCommonModel
import com.konai.vam.core.enumerate.WooriBankMessage
import com.konai.vam.core.restclient.wooribank.PostWooriWorkRequest
import com.konai.vam.core.restclient.wooribank.WooriBankRestClient
import org.springframework.stereotype.Service

@Service
class WooriBankWorkService(
    private val wooriBankRestClient: WooriBankRestClient,
) : WooriBankWorkAdapter {

    override fun work(message: WooriBankMessage) {
        wooriBankRestClient.postWooriBankWork(request = PostWooriWorkRequest(WooriBankCommonModel(message.requestCode)))
    }

}