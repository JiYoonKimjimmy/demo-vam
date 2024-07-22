package com.konai.vam.api.v1.wooribank.service.management

import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagement
import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagementMapper
import com.konai.vam.core.repository.wooribank.management.WooriBankManagementEntityAdapter
import com.konai.vam.core.repository.wooribank.management.jdsl.WooriBankManagementPredicate
import org.springframework.stereotype.Service

@Service
class WooriBankManagementFindService(
    private val wooriBankManagementMapper: WooriBankManagementMapper,
    private val wooriBankManagementEntityAdapter: WooriBankManagementEntityAdapter
) : WooriBankManagementFindAdapter {

    override fun findByPredicate(predicate: WooriBankManagementPredicate): WooriBankManagement? {
        return wooriBankManagementEntityAdapter.findByPredicate(predicate).orElse(null)
            ?.let { wooriBankManagementMapper.entityToDomain(it) }
    }

}