package com.konai.vam.core.restclient.cardse

import com.konai.vam.core.common.restclient.BaseRestClient
import com.konai.vam.core.common.restclient.ComponentName
import org.springframework.stereotype.Component

@Component
class CardSeRestClient : BaseRestClient() {

    override val baseUrl: String by lazy { generateBaseUrl(ComponentName.CARD_SE) }

    fun getCardsInfoBatchId(request: CardSeGetCardsInfoBatchIdRequest): CardSeGetCardsInfoBatchIdResponse {
        return get(
            url = "$baseUrl${request.url}",
            response = CardSeGetCardsInfoBatchIdResponse::class.java
        )
    }

    fun getCardsInfoToken(request: CardSeGetCardsInfoTokenRequest): CardSeGetCardsInfoTokenResponse {
        return get(
            url = "$baseUrl${request.url}",
            response = CardSeGetCardsInfoTokenResponse::class.java
        )
    }

}