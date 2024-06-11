package com.konai.vam.api.v1.virtualaccount.service.domain

import com.konai.vam.core.enumerate.VirtualAccountMappingType
import com.konai.vam.core.enumerate.VirtualAccountStatus
import com.konai.vam.core.enumerate.YesOrNo

data class VirtualAccount(
    var id: Long? = null,
    val accountNumber: String,
    val bankCode: String,
    val bankName: String,
    val mappingType: VirtualAccountMappingType,
    val mappingYn: YesOrNo,
    val status: VirtualAccountStatus,
)