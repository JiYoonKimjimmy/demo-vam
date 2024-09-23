package com.konai.vam.api.v1.parentaccount.service.domain

import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity
import org.springframework.stereotype.Component

@Component
class ParentAccountMapper {

    fun domainToEntity(domain: ParentAccount): ParentAccountEntity {
        return ParentAccountEntity(
            id = domain.id,
            parentAccountNo = domain.parentAccountNo,
            bankCode = domain.bankCode,
        )
    }

    fun entityToDomain(entity: ParentAccountEntity): ParentAccount {
        return ParentAccount(
            id = entity.id,
            parentAccountNo = entity.parentAccountNo,
            bankCode = entity.bankCode,
        )
    }

}