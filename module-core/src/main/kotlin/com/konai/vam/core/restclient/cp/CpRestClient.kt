package com.konai.vam.core.restclient.cp

import com.konai.vam.core.common.restclient.BaseRestClient
import com.konai.vam.core.common.restclient.ComponentName
import org.springframework.stereotype.Component

@Component
class CpRestClient : BaseRestClient() {

    override val baseUrl: String by lazy { generateBaseUrl(ComponentName.CP) }

    fun getCardsToken(request: CpGetCardsTokenRequest): CpGetCardsTokenResponse {
        return get(
            url = "$baseUrl${request.url}",
            response = CpGetCardsTokenResponse::class.java
        )
    }

}