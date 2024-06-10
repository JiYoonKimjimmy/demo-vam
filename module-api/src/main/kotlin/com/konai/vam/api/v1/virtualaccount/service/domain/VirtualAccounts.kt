package com.konai.vam.api.v1.virtualaccount.service.domain

import com.konai.vam.core.common.model.BasePageable

data class VirtualAccounts(
    val pageable: BasePageable.Pageable,
    val content: List<VirtualAccount>
)