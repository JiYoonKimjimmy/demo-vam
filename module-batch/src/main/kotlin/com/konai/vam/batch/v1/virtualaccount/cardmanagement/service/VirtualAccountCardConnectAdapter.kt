package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory

interface VirtualAccountCardConnectAdapter {

    fun connectVirtualAccountCard(batchId:String, serviceId: String, bankCode: String, parList: List<String>): VirtualAccountBatchHistory

}