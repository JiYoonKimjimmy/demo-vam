package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount
import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccountMapper
import com.konai.vam.core.repository.parentaccount.ParentAccountEntityAdapter
import org.springframework.stereotype.Service

@Service
class ParentAccountManagementService(
    private val parentAccountMapper: ParentAccountMapper,
    private val parentAccountEntityAdapter: ParentAccountEntityAdapter
) : ParentAccountManagementAdapter {

    override fun save(domains: ParentAccount): ParentAccount {
        return parentAccountMapper.domainToEntity(domains)
            .let { parentAccountEntityAdapter.checkDuplicated(it) }
            .let { parentAccountEntityAdapter.save(it) }
            .let { parentAccountMapper.entityToDomain(it) }
    }

}