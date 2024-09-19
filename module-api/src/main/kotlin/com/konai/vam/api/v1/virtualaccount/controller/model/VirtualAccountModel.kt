package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.core.enumerate.VirtualAccountConnectType
import com.konai.vam.core.enumerate.VirtualAccountStatus

data class VirtualAccountModel(
    val accountNo: String,
    val bankCode: String,
    val connectType: VirtualAccountConnectType,
    val status: VirtualAccountStatus,
    val par: String?
)