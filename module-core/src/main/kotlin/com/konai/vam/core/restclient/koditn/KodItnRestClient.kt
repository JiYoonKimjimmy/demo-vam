package com.konai.vam.core.restclient.koditn

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.konai.vam.core.common.restclient.BaseRestClient
import com.konai.vam.core.common.restclient.ComponentName
import org.springframework.stereotype.Component

@Component
class KodItnRestClient : BaseRestClient() {

    private final val baseUrl: String by lazy { generateBaseUrl(ComponentName.KOD_ITN) }

    fun getMinimalInfoList(request: KodItnGetMinimalInfoListRequest): List<KodItnProduct> {
        return restClient
            .post()
            .uri("$baseUrl${request.url}")
            .body(jacksonObjectMapper().writeValueAsString(request.serviceIds))
            .retrieve()
            .toEntity(Array<KodItnProduct>::class.java)
            .body
            ?.toList()
            ?: emptyList()
    }

}