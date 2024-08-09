package com.konai.vam.batch.v1.virtualaccount.batchfile.service

import com.konai.vam.batch.v1.virtualaccount.batchfile.service.domain.VirtualAccountBatchFile

interface VirtualAccountBatchFileDownloadAdapter {

    fun downloadBulkCardFile(batchId: String): VirtualAccountBatchFile

}