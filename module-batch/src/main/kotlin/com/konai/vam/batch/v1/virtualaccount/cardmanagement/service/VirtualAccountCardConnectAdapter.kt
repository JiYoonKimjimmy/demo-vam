package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain.VirtualAccountCardConnect

interface VirtualAccountCardConnectAdapter {

    fun connectCardToVirtualAccounts(domain: VirtualAccountCardConnect): VirtualAccountBatchHistory

}