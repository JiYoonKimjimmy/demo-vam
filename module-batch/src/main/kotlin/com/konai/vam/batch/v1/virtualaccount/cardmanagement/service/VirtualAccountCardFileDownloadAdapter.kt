package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

import com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain.VirtualAccountCardFile

interface VirtualAccountCardFileDownloadAdapter {

    fun downloadBulkCardFile(batchId: String): VirtualAccountCardFile

}