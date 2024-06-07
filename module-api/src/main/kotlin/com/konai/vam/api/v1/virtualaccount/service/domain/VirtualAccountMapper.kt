package com.konai.vam.api.v1.virtualaccount.service.domain

import com.konai.vam.core.virtualaccount.repository.entity.VirtualAccountEntity
import org.springframework.stereotype.Component

@Component
class VirtualAccountMapper {

    fun domainToEntity(domain: VirtualAccount): VirtualAccountEntity {
        return VirtualAccountEntity(
            accountNumber = domain.accountNumber,
            bankCode = domain.bankCode,
            bankName = domain.bankName,
            status = domain.status,
        )
    }

    fun entityToDomain(entity: VirtualAccountEntity): VirtualAccount {
        return VirtualAccount(
            id = entity.id,
            accountNumber = entity.accountNumber,
            bankCode = entity.bankCode,
            bankName = entity.bankName,
            status = entity.status,
        )
    }

}