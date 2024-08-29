package com.konai.vam.core.restclient.wooribank

import com.konai.vam.core.common.model.wooribank.WooriBankAggregationMessage
import com.konai.vam.core.common.model.wooribank.WooriBankMessage
import com.konai.vam.core.common.restclient.BaseRestClient
import com.konai.vam.core.common.restclient.ComponentName
import org.springframework.stereotype.Component

@Component
class WooriBankRestClient : BaseRestClient() {

    override val baseUrl: String by lazy { generateBaseUrl(ComponentName.FEP) }

    fun postWooriBankWork(message: WooriBankMessage): WooriBankMessage {
        return post(
            url = baseUrl,
            body = message,
            response = WooriBankMessage::class.java
        )
    }

    fun postWooriAggregateTransaction(message: WooriBankAggregationMessage): WooriBankAggregationMessage {
        return post(
            url = baseUrl,
            body = message,
            response = WooriBankAggregationMessage::class.java
        )
    }

}