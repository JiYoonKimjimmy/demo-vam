package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount

interface VirtualAccountSaveAdapter {

    fun save(domain: VirtualAccount): VirtualAccount

    fun saveAll(domains: List<VirtualAccount>): List<VirtualAccount>
    
}