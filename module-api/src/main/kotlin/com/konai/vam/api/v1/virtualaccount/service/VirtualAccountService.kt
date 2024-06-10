package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccounts
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.virtualaccount.VirtualAccountRepository
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

    override fun getById(id: Long): VirtualAccount {
        TODO("Not yet implemented")
    }

    override fun findAll(pageable: PageableRequest): VirtualAccounts {
        return virtualAccountRepository.findAll(pageable)
            .let { virtualAccountMapper.pageableToDomain(it) }
    }

}