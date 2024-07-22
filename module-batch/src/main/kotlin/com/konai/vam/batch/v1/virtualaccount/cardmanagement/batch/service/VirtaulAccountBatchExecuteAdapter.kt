package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.service

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory

interface VirtaulAccountBatchExecuteAdapter {

    fun executeCreateSemFileBatchJob(batchId: String, batchHistory: VirtualAccountBatchHistory): String

}