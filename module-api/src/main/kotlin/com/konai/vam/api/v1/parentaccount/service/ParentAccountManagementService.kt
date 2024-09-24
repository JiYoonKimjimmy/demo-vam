package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import org.springframework.stereotype.Service

@Service
class ParentAccountManagementService(
    private val parentAccountSaveAdapter: ParentAccountSaveAdapter,
    private val parentAccountFindAdapter: ParentAccountFindAdapter
) : ParentAccountManagementAdapter {

    override fun create(domain: ParentAccount): ParentAccount {
        return parentAccountSaveAdapter.save(domain)
    }

    override fun update(domain: ParentAccount): ParentAccount {
        return parentAccountFindAdapter.findOne(ParentAccountPredicate(id = domain.id))
            .updateParentAccountNoOrBankCode(domain.parentAccountNo, domain.bankCode)
            .let { parentAccountSaveAdapter.save(it) }
    }

}