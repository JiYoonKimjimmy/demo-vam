package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.virtualaccount.VirtualAccountEntityAdapter
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import org.springframework.stereotype.Service

@Service
class VirtualAccountService(
    private val virtualAccountEntityAdapter: VirtualAccountEntityAdapter,
    private val virtualAccountMapper: VirtualAccountMapper
) : VirtualAccountAdapter {

    override fun create(account: VirtualAccount): VirtualAccount {
        return virtualAccountMapper.domainToEntity(account)
            .let { virtualAccountEntityAdapter.save(it) }
            .let { virtualAccountMapper.entityToDomain(it) }
    }

    override fun findOne(predicate: VirtualAccountPredicate): VirtualAccount {
        return virtualAccountEntityAdapter.findByPredicate(predicate)
            .orElseThrow { ResourceNotFoundException(ErrorCode.VIRTUAL_ACCOUNT_NOT_FOUND) }
            .let { virtualAccountMapper.entityToDomain(it) }
    }

    override fun findPage(predicate: VirtualAccountPredicate, pageable: PageableRequest): BasePageable<VirtualAccount> {
        return virtualAccountEntityAdapter.findAllByPredicate(predicate, pageable)
            .let { virtualAccountMapper.entitiesToPageable(it) }
    }

}