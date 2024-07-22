package com.konai.vam.batch.v1.virtualaccount.batchhistory.service

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory

interface VirtualAccountBatchHistorySaveAdapter {

    fun save(domain: VirtualAccountBatchHistory): VirtualAccountBatchHistory

    fun saveAndFlush(domain: VirtualAccountBatchHistory): VirtualAccountBatchHistory

}