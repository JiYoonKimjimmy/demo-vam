package com.konai.vam.api.v1.wooribank.service.management

import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagement
import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagementMapper
import com.konai.vam.core.repository.wooribank.management.WooriBankManagementEntityAdapter
import org.springframework.stereotype.Service

@Service
class WooriBankManagementSaveService(
    private val wooriBankManagementMapper: WooriBankManagementMapper,
    private val wooriBankManagementEntityAdapter: WooriBankManagementEntityAdapter
) : WooriBankManagementSaveAdapter {

    override fun save(domain: WooriBankManagement): WooriBankManagement {
        return wooriBankManagementMapper.domainToEntity(domain)
            .let { wooriBankManagementEntityAdapter.save(it) }
            .let { wooriBankManagementMapper.entityToDomain(it) }
    }

}