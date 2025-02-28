package com.konai.vam.api.v1.virtualaccount.service.domain

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccountMapper
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import org.springframework.stereotype.Component

@Component
class VirtualAccountMapper(
    private val parentAccountMapper: ParentAccountMapper
) {

    fun domainToEntity(domain: VirtualAccount): VirtualAccountEntity {
        return VirtualAccountEntity(
            id = domain.id,
            accountNo = domain.bankAccount.accountNo,
            bankCode = domain.bankAccount.bankCode,
            connectType = domain.connectType,
            status = domain.status,
            par = domain.par,
            serviceId = domain.serviceId,
            cardConnectStatus = domain.cardConnectStatus,
            cardConnected = domain.cardConnected,
            cardDisconnected = domain.cardDisconnected,
            cardSeBatchId = domain.cardSeBatchId,
            parentAccount = parentAccountMapper.domainToEntity(domain.getParentAccount())
        )
    }

    fun entityToDomain(entity: VirtualAccountEntity): VirtualAccount {
        return VirtualAccount(
                id = entity.id,
                bankAccount = BankAccount(entity.bankCode, entity.accountNo),
                connectType = entity.connectType,
                status = entity.status,
                par = entity.par,
                serviceId = entity.serviceId,
                cardConnectStatus = entity.cardConnectStatus,
                cardConnected = entity.cardConnected,
                cardDisconnected = entity.cardDisconnected,
                cardSeBatchId = entity.cardSeBatchId
            )
            .setParentAccount { parentAccountMapper.entityToDomain(entity.parentAccount) }
    }

    fun entitiesToPageable(entities: BasePageable<VirtualAccountEntity?>): BasePageable<VirtualAccount> {
        return BasePageable(
            pageable = entities.pageable,
            content = entities.content.filterNotNull().map(this::entityToDomain)
        )
    }

    fun domainToEntities(domains: List<VirtualAccount>): List<VirtualAccountEntity> {
        return domains.map { domainToEntity(it) }
    }

    fun entityToDomains(entities: List<VirtualAccountEntity>): List<VirtualAccount> {
        return entities.map { entityToDomain(it) }
    }

}