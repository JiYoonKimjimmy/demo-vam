package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

interface VirtualAccountCardManagementAdapter {

    fun connectBulkCard(batchId: String, serviceId: String)

}