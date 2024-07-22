package com.konai.vam.api.v1.rechargetransaction.service

import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransaction
import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransactionMapper
import com.konai.vam.core.repository.rechargetransaction.RechargeTransactionEntityAdapter
import org.springframework.stereotype.Service

@Service
class RechargeTransactionSaveService(
    private val rechargeTransactionEntityAdapter: RechargeTransactionEntityAdapter,
    private val rechargeTransactionMapper: RechargeTransactionMapper,
) : RechargeTransactionSaveAdapter {

    override fun save(domain: RechargeTransaction): RechargeTransaction {
        return rechargeTransactionMapper.domainToEntity(domain)
            .let { rechargeTransactionEntityAdapter.save(it) }
            .let { rechargeTransactionMapper.entityToDomain(it) }
    }

}