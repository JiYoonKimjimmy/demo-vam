package com.konai.vam.api.v1.virtualaccountbank.service.domain

data class VirtualAccountBank(
    val bankCode: String,
    val bankName: String,
    val bankEngName: String,
    val rechargerId: String,
)