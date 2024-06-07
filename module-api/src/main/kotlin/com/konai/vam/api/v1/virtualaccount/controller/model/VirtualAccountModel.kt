package com.konai.vam.api.v1.virtualaccount.controller.model

data class VirtualAccountModel(
    val id: Long? = null,
    val accountNumber: String,
    val bankCode: String,
    val bankName: String,
    val status: String,
)