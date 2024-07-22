package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory

interface VirtualAccountCardManagementAdapter {

    fun connectBulkCard(batchId: String, serviceId: String)

    fun createBulkCardFile(batchId: String, batchHistory: VirtualAccountBatchHistory): String

}