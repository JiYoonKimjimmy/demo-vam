package com.konai.vam.api.v1.virtualaccountbank.service

import com.konai.vam.api.v1.virtualaccountbank.service.domain.VirtualAccountBank
import com.konai.vam.api.v1.virtualaccountbank.service.domain.VirtualAccountBankMapper
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.repository.virtualaccountbank.VirtualAccountBankEntityAdapter
import com.konai.vam.core.repository.virtualaccountbank.jdsl.VirtualAccountBankPredicate
import org.springframework.stereotype.Service

@Service
class VirtualAccountBankFindService(
    private val virtualAccountBankEntityAdapter: VirtualAccountBankEntityAdapter,
    private val virtualAccountBankMapper: VirtualAccountBankMapper,
) : VirtualAccountBankFindAdapter {

    override fun findByBankCode(bankCode: String): VirtualAccountBank {
        return virtualAccountBankEntityAdapter.findByBankCode(bankCode)
            .let { virtualAccountBankMapper.entityToDomain(it) }
    }

    override fun findAllByPredicate(predicate: VirtualAccountBankPredicate): BasePageable<VirtualAccountBank> {
        return virtualAccountBankEntityAdapter.findAllByPredicate(predicate)
            .let { virtualAccountBankMapper.entitiesToDomain(it) }
    }

}