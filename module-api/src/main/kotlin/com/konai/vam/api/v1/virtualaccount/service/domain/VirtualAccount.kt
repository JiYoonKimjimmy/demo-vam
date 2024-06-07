package com.konai.vam.api.v1.virtualaccount.service.domain

data class VirtualAccount(
    var id: Long? = null,
    val accountNumber: String,
    val bankCode: String,
    val bankName: String,
    val status: String,
)