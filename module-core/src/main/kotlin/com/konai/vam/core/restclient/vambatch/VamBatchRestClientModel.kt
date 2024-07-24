package com.konai.vam.core.restclient.vambatch

data class VamBatchVirtualAccountBulkCardConnectRequest(
    val batchId: String,
    val serviceId: String,
) {
    val url by lazy { "/api/v1/batch/internal/virtual-account/bulk/card/connect" }
}

data class VamBatchDownloadVirtualAccountBulkCardFileRequest(
    val batchId: String,
) {
    val url by lazy { "/api/v1/batch/internal/virtual-account/bulk/card/download/file/$batchId" }
}