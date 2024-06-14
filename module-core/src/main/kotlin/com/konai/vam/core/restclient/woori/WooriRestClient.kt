package com.konai.vam.core.restclient.woori

import com.konai.vam.core.common.restclient.BaseRestClient
import com.konai.vam.core.common.restclient.ComponentName
import org.springframework.stereotype.Component

@Component
class WooriRestClient : BaseRestClient() {

    override val baseUrl: String by lazy { generateBaseUrl(ComponentName.FEP) }

    fun postWooriWorkStart() {

    }

    fun postWooriWorkStop() {

    }

    fun getWooriAggregateTransaction() {

    }

}