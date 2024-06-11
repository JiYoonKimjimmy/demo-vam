package com.konai.vam.api.v1.virtualaccount.service.domain

import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import org.springframework.stereotype.Component

@Component
class VirtualAccountMapper {

    fun domainToEntity(domain: VirtualAccount): VirtualAccountEntity {
        return VirtualAccountEntity(
            accountNumber = domain.accountNumber,
            bankCode = domain.bankCode,
            bankName = domain.bankName,
            mappingType = domain.mappingType,
            mappingYn = domain.mappingYn,
            status = domain.status,
        )
    }

    fun entityToDomain(entity: VirtualAccountEntity): VirtualAccount {
        return VirtualAccount(
            id = entity.id,
            accountNumber = entity.accountNumber,
            bankCode = entity.bankCode,
            bankName = entity.bankName,
            mappingType = entity.mappingType,
            mappingYn = entity.mappingYn,
            status = entity.status,
        )
    }

    fun entitiesToDomain(entities: BasePageable<VirtualAccountEntity?>): BasePageable<VirtualAccount> {
        return BasePageable(
            pageable = entities.pageable,
            content = entities.content.filterNotNull().map(this::entityToDomain)
        )
    }

}