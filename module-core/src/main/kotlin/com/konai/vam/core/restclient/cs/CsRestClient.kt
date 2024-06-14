package com.konai.vam.core.restclient.cs

import com.konai.vam.core.common.restclient.BaseRestClient
import com.konai.vam.core.common.restclient.ComponentName
import org.springframework.stereotype.Component

@Component
class CsRestClient : BaseRestClient() {

    override val baseUrl: String by lazy { generateBaseUrl(ComponentName.CS) }

    fun postRechargesSystemManuals(request: CsPostRechargesSystemManualsRequest): CsPostRechargesSystemManualsResponse {
        return post(
            url = "$baseUrl${request.url}",
            body = request,
            response = CsPostRechargesSystemManualsResponse::class.java
        )
    }

    fun postRechargesSystemManualsReversal(request: CsPostRechargesSystemManualsReversalRequest): CsPostRechargesSystemManualsReversalResponse {
        return post(
            url = "$baseUrl${request.url}",
            body = request,
            response = CsPostRechargesSystemManualsReversalResponse::class.java
        )
    }

}