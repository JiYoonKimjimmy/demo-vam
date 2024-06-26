package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.virtualaccount.VirtualAccountRepository
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import org.springframework.stereotype.Service

@Service
class VirtualAccountService(
    private val virtualAccountRepository: VirtualAccountRepository,
    private val virtualAccountMapper: VirtualAccountMapper
) : VirtualAccountUseCase {

    override fun create(account: VirtualAccount): VirtualAccount {
        return virtualAccountMapper.domainToEntity(account)
            .let { virtualAccountRepository.save(it) }
            .let { virtualAccountMapper.entityToDomain(it) }
    }

    override fun findPage(predicate: VirtualAccountPredicate, pageable: PageableRequest): BasePageable<VirtualAccount> {
        return virtualAccountRepository.findPage(predicate, pageable)
            .let { virtualAccountMapper.entitiesToDomain(it) }
    }
}