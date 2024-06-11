package com.konai.vam.core.repository.virtualaccount.jdsl

import com.konai.vam.core.enumerate.VirtualAccountMappingType

data class VirtualAccountPredicate(
    val accountNumber: String? = null,
    val bankCode: String? = null,
    val mappingType: VirtualAccountMappingType? = null,
    val isMapping: Boolean? = false,
)