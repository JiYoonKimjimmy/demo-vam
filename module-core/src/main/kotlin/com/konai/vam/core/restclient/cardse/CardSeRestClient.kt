package com.konai.vam.core.restclient.cardse

import com.konai.vam.core.common.restclient.BaseRestClient
import com.konai.vam.core.common.restclient.ComponentName
import org.springframework.stereotype.Component

@Component
class CardSeRestClient : BaseRestClient() {

    override val baseUrl: String by lazy { generateBaseUrl(ComponentName.CARD_SE) }

    fun getCardsInfoBatchId(request: CardSeGetCardsInfoBatchIdRequest): CardSeGetCardsInfoBatchIdResponse? {
        return restClient
            .get()
            .uri("$baseUrl${request.url}")
            .retrieve()
            .toEntity(CardSeGetCardsInfoBatchIdResponse::class.java)
            .body
    }

    fun getCardsInfoToken(request: CardSeGetCardsInfoTokenRequest): CardSeGetCardsInfoTokenResponse? {
        return restClient
            .get()
            .uri("$baseUrl${request.url}")
            .retrieve()
            .toEntity(CardSeGetCardsInfoTokenResponse::class.java)
            .body
    }

}