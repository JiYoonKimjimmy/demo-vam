package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.parentaccount.service.ParentAccountFindAdapter
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import com.konai.vam.core.repository.virtualaccount.VirtualAccountEntityAdapter
import org.springframework.stereotype.Service

@Service
class VirtualAccountWriteService(
    private val virtualAccountMapper: VirtualAccountMapper,
    private val virtualAccountEntityAdapter: VirtualAccountEntityAdapter,
    private val parentAccountFindAdapter: ParentAccountFindAdapter
) : VirtualAccountWriteAdapter {

    override fun create(domain: VirtualAccount): VirtualAccount {
        return domain
            .setParentAccount { parentAccountFindAdapter.findOne(ParentAccountPredicate(id = domain.parentAccountId)) }
            .let { virtualAccountMapper.domainToEntity(it) }
            .let { virtualAccountEntityAdapter.save(it) }
            .let { virtualAccountMapper.entityToDomain(it) }
    }

}