package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount

interface VirtualAccountSaveAdapter {

    fun saveAll(domains: List<VirtualAccount>): List<VirtualAccount>
    
}