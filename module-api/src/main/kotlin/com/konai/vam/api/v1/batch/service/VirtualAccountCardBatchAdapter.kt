package com.konai.vam.api.v1.batch.service

import org.springframework.core.io.Resource

interface VirtualAccountCardBatchAdapter {

    fun virtualAccountBulkCardConnect(batchId: String, serviceId: String)

    fun downloadVirtualAccountBulkCardFile(batchId: String): Resource

}