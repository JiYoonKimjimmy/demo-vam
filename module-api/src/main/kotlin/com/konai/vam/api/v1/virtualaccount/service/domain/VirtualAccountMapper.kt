package com.konai.vam.api.v1.virtualaccount.service.domain

import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import org.springframework.stereotype.Component

@Component
class VirtualAccountMapper {

    fun domainToEntity(domain: VirtualAccount): VirtualAccountEntity {
        return VirtualAccountEntity(
            accountNo = domain.accountNo,
            bankCode = domain.bankCode,
            connectType = domain.connectType,
            status = domain.status,
            par = domain.par,
            cardConnectStatus = domain.cardConnectStatus,
            cardConnected = domain.cardConnected,
            cardDisconnected = domain.cardDisconnected,
            cardSeBatchId = domain.cardSeBatchId,
        )
    }

    fun entityToDomain(entity: VirtualAccountEntity): VirtualAccount {
        return VirtualAccount(
            accountNo = entity.accountNo,
            bankCode = entity.bankCode,
            connectType = entity.connectType,
            status = entity.status,
            par = entity.par,
            cardConnectStatus = entity.cardConnectStatus,
            cardConnected = entity.cardConnected,
            cardDisconnected = entity.cardDisconnected,
            cardSeBatchId = entity.cardSeBatchId,
        )
    }

    fun entitiesToDomain(entities: BasePageable<VirtualAccountEntity?>): BasePageable<VirtualAccount> {
        return BasePageable(
            pageable = entities.pageable,
            content = entities.content.filterNotNull().map(this::entityToDomain)
        )
    }

}