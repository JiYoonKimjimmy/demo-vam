package com.konai.vam.core.repository.virtualaccount.jdsl

import com.konai.vam.core.enumerate.VirtualAccountConnectType

data class VirtualAccountPredicate(
    val accountNo: String? = null,
    val bankCode: String? = null,
    val connectType: VirtualAccountConnectType? = null,
    val isMapping: Boolean? = false,
)