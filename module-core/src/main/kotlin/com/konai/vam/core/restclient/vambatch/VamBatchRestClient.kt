package com.konai.vam.core.restclient.vambatch

import com.konai.vam.core.common.model.VoidResponse
import com.konai.vam.core.common.restclient.BaseRestClient
import com.konai.vam.core.common.restclient.ComponentName
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

@Component
class VamBatchRestClient : BaseRestClient() {

    override val baseUrl: String by lazy { generateBaseUrl(ComponentName.VAM_BATCH) }

    fun virtualAccountBulkCardConnect(request: VamBatchVirtualAccountBulkCardConnectRequest): VoidResponse {
        return post(
            url = "$baseUrl${request.url}",
            body = request,
            response = VoidResponse::class.java
        )
    }

    fun downloadVirtualAccountBulkCardFile(request: VamBatchDownloadVirtualAccountBulkCardFileRequest): Resource {
        return get(
            url = "$baseUrl${request.url}",
            response = Resource::class.java
        )
    }

}