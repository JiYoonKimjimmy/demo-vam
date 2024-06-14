package com.konai.vam.api.v1.virtualaccount.service.domain

import com.konai.vam.core.enumerate.VirtualAccountConnectType
import com.konai.vam.core.enumerate.VirtualAccountStatus

data class VirtualAccount(
    var id: Long? = null,
    val accountNo: String,
    val bankCode: String,
    val connectType: VirtualAccountConnectType,
    val status: VirtualAccountStatus,
)