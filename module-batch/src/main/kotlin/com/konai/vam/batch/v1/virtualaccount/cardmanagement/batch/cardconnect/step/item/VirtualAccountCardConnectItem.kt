package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step.item

data class VirtualAccountCardConnectItem(
    val id: Long,
    val serviceId: String,
    val accountNo: String,
    val par: String,
)