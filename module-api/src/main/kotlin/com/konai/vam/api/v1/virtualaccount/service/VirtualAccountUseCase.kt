package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount

interface VirtualAccountUseCase {

    fun create(account: VirtualAccount): VirtualAccount
    fun getById(id: Long): VirtualAccount

}