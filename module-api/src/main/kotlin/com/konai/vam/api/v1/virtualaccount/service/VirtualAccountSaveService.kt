package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.repository.virtualaccount.VirtualAccountEntityAdapter
import org.springframework.stereotype.Service

@Service
class VirtualAccountSaveService(
    private val virtualAccountMapper: VirtualAccountMapper,
    private val virtualAccountEntityAdapter: VirtualAccountEntityAdapter
) : VirtualAccountSaveAdapter {

    override fun save(domain: VirtualAccount): VirtualAccount {
        return if (virtualAccountEntityAdapter.existsByAccountNoAndBankCode(domain.bankAccount.accountNo, domain.bankAccount.bankCode)) {
            throw InternalServiceException(ErrorCode.VIRTUAL_ACCOUNT_IS_DUPLICATED)
        } else {
            virtualAccountMapper.domainToEntity(domain)
                .let { virtualAccountEntityAdapter.save(it) }
                .let { virtualAccountMapper.entityToDomain(it) }
        }
    }

    override fun saveAll(domains: List<VirtualAccount>): List<VirtualAccount> {
        return virtualAccountMapper.domainToEntities(domains)
            .let { virtualAccountEntityAdapter.saveAll(it) }
            .let { virtualAccountMapper.entityToDomains(it) }
    }
}