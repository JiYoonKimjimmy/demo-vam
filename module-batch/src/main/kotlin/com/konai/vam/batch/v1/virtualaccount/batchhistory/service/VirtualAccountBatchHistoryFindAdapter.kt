package com.konai.vam.batch.v1.virtualaccount.batchhistory.service

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory

interface VirtualAccountBatchHistoryFindAdapter {

    fun findByBatchId(batchId: String) : VirtualAccountBatchHistory

}