package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

interface VirtualAccountCardFetchAdapter {

    fun fetchParsByBatchId(batchId: String): List<String>

}