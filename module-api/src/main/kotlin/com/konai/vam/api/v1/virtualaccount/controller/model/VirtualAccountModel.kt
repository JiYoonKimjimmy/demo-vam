package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.core.enumerate.VirtualAccountStatus

data class VirtualAccountModel(
    val id: Long? = null,
    val accountNo: String,
    val bankCode: String,
    val status: VirtualAccountStatus,
)