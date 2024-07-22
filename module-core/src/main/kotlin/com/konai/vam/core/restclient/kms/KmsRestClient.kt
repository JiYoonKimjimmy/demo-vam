package com.konai.vam.core.restclient.kms

import com.konai.vam.core.common.restclient.BaseRestClient
import com.konai.vam.core.common.restclient.ComponentName
import org.springframework.stereotype.Component

@Component
class KmsRestClient : BaseRestClient() {

    override val baseUrl: String by lazy { generateBaseUrl(ComponentName.KMS) }

    fun getEncryptionKey(request: KmsGetEncryptionKeyRequest): KmsGetEncryptionKeyResponse {
        return post(
            url = "$baseUrl${request.url}",
            body = request,
            response = KmsGetEncryptionKeyResponse::class.java
        )
    }

}