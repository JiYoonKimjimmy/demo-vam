package com.konai.vam.core.restclient.koditn

import com.konai.vam.core.common.restclient.BaseRestClient
import com.konai.vam.core.common.restclient.ComponentName
import org.springframework.stereotype.Component

@Component
class KodItnRestClient : BaseRestClient() {

    override val baseUrl: String by lazy { generateBaseUrl(ComponentName.KOD_ITN) }

    fun getProductsBasicInfo(request: KodItnGetProductsBasicInfoRequest): KodItnProduct {
        return get(
            url = "$baseUrl${request.url}",
            response = KodItnProduct::class.java
        )
    }

}