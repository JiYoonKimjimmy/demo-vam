package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.parentaccount.service.ParentAccountFindAdapter
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import org.springframework.stereotype.Service

@Service
class VirtualAccountManageService(
    private val virtualAccountSaveAdapter: VirtualAccountSaveAdapter,
    private val parentAccountFindAdapter: ParentAccountFindAdapter
) : VirtualAccountManageAdapter {

    override fun create(domain: VirtualAccount): VirtualAccount {
        return domain
            .setParentAccount { parentAccountFindAdapter.findOne(ParentAccountPredicate(id = domain.parentAccountId)) }
            .let { virtualAccountSaveAdapter.save(it) }
    }

}