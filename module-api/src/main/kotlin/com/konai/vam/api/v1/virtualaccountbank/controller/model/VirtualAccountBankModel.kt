package com.konai.vam.api.v1.virtualaccountbank.controller.model

data class VirtualAccountBankModel(
    val bankCode: String,
    val bankName: String,
    val bankEngName: String,
    val rechargerId: String
)