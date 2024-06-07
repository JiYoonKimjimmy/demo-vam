package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.core.virtualaccount.repository.VirtualAccountRepository
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

}