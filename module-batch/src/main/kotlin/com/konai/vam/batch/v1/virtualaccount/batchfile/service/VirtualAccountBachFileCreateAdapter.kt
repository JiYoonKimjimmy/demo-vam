package com.konai.vam.batch.v1.virtualaccount.batchfile.service

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory

interface VirtualAccountBachFileCreateAdapter {

    fun createBatchFile(batchId: String, batchHistory: VirtualAccountBatchHistory): String

}