package com.konai.vam.core.repository.virtualaccount.jdsl

import com.konai.vam.core.enumerate.VirtualAccountConnectType

data class VirtualAccountPredicate(
    val accountNumber: String? = null,
    val bankCode: String? = null,
    val mappingType: VirtualAccountConnectType? = null,
    val isMapping: Boolean? = false,
)