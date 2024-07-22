package com.konai.vam.api.v1.batch.service

import com.konai.vam.core.restclient.vambatch.VamBatchDownloadVirtualAccountBulkCardFileRequest
import com.konai.vam.core.restclient.vambatch.VamBatchRestClient
import com.konai.vam.core.restclient.vambatch.VamBatchVirtualAccountBulkCardConnectRequest
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class VirtualAccountCardBatchService(
    private val vamBatchRestClient: VamBatchRestClient
) : VirtualAccountCardBatchAdapter {

    override fun virtualAccountBulkCardConnect(batchId: String, serviceId: String) {
        vamBatchRestClient.virtualAccountBulkCardConnect(request = VamBatchVirtualAccountBulkCardConnectRequest(batchId, serviceId))
    }

    override fun downloadVirtualAccountBulkCardFile(batchId: String): Resource {
        return vamBatchRestClient.downloadVirtualAccountBulkCardFile(request = VamBatchDownloadVirtualAccountBulkCardFileRequest((batchId)))
    }

}