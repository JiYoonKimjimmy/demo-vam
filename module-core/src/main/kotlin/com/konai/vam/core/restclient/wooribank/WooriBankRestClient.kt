package com.konai.vam.core.restclient.wooribank

import com.konai.vam.core.common.restclient.BaseRestClient
import com.konai.vam.core.common.restclient.ComponentName
import org.springframework.stereotype.Component

@Component
class WooriBankRestClient : BaseRestClient() {

    override val baseUrl: String by lazy { generateBaseUrl(ComponentName.FEP) }

    fun postWooriBankWork(request: PostWooriWorkRequest): PostWooriWorkResponse {
        return post(
            url = baseUrl,
            body = request,
            response = PostWooriWorkResponse::class.java
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