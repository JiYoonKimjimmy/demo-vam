package com.konai.vam.api.v1.parentaccount.service.domain

data class ParentAccount(
    val id: Long? = null,
    val parentAccountNo: String,
    val bankCode: String
)