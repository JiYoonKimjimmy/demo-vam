package com.konai.vam.core.restclient.wooribank

import com.konai.vam.core.common.model.wooribank.WooriBankCommonMessage
import com.konai.vam.core.common.restclient.BaseRestClient
import com.konai.vam.core.common.restclient.ComponentName
import org.springframework.stereotype.Component

@Component
class WooriBankRestClient : BaseRestClient() {

    override val baseUrl: String by lazy { generateBaseUrl(ComponentName.FEP) }

    fun postWooriBankWork(request: PostWooriWorkRequest): WooriBankCommonMessage {
        return post(
            url = baseUrl,
            body = request,
            response = WooriBankCommonMessage::class.java
        )
    }

    fun postWooriAggregateTransaction(request: PostWooriAggregateTransactionRequest): PostWooriAggregateTransactionResponse {
        return post(
            url = baseUrl,
            body = request,
            response = PostWooriAggregateTransactionResponse::class.java
        )
    }

}