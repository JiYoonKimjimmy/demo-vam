package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.service

interface VirtualAccountBatchExecuteAdapter {

    fun executeCreateSemFileBatchJob(batchId: String, serviceId: String, quantity: Int): String

}