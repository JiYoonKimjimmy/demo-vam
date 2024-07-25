package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory

data class VirtualAccountCardConnect(
    val batchId: String,
    val serviceId: String,
    val bankCode: String,
    val pars: List<String>
) {

    lateinit var batchHistory: VirtualAccountBatchHistory
    fun setBatchHistory(batchHistory: VirtualAccountBatchHistory): VirtualAccountCardConnect {
        return this.apply { this.batchHistory = batchHistory }
    }

}