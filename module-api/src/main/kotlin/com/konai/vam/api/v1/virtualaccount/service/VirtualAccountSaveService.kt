package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.core.repository.virtualaccount.VirtualAccountEntityAdapter
import org.springframework.stereotype.Service

@Service
class VirtualAccountSaveService(
    private val virtualAccountEntityAdapter: VirtualAccountEntityAdapter,
    private val virtualAccountMapper: VirtualAccountMapper
) : VirtualAccountSaveAdapter {

    override fun saveAll(domains: List<VirtualAccount>): List<VirtualAccount> {
        return virtualAccountMapper.domainToEntities(domains)
            .let { virtualAccountEntityAdapter.saveAll(it) }
            .let { virtualAccountMapper.entityToDomains(it) }
    }
}