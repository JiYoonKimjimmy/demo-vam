package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain

data class VirtualAccountCardConnect(
    val batchId: String,
    val serviceId: String,
    val bankCode: String,
    val pars: List<String>
)