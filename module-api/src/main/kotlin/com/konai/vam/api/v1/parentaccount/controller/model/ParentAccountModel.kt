package com.konai.vam.api.v1.parentaccount.controller.model

data class ParentAccountModel(
    val parentAccountId: Long? = null,
    val parentAccountNo: String,
    val bankCode: String
)