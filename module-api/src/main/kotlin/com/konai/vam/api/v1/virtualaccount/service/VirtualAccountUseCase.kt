package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccounts
import com.konai.vam.core.common.model.PageableRequest

interface VirtualAccountUseCase {

    fun create(account: VirtualAccount): VirtualAccount

    fun getById(id: Long): VirtualAccount

    fun findAll(pageable: PageableRequest): VirtualAccounts

}