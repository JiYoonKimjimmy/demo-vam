package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount

interface VirtualAccountManageAdapter {

    fun create(domain: VirtualAccount): VirtualAccount

}