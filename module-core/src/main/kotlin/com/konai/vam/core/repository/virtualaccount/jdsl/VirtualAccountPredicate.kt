package com.konai.vam.core.repository.virtualaccount.jdsl

data class VirtualAccountPredicate(
    val accountNumber: String? = null,
    val bankCode: String? = null,
    val mappingType: String? = null,
    val isMapping: Boolean? = false,
)