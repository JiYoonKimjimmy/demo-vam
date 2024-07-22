package com.konai.vam.api.v1.virtualaccountbank.service.domain

import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.repository.virtualaccountbank.entity.VirtualAccountBankEntity
import org.springframework.stereotype.Component

@Component
class VirtualAccountBankMapper {

    fun entityToDomain(entity: VirtualAccountBankEntity): VirtualAccountBank {
        return VirtualAccountBank(
            bankCode = entity.bankCode,
            bankName = entity.bankName,
            bankEngName = entity.bankEngName,
            rechargerId = entity.rechargerId,
        )
    }

    fun entitiesToDomain(entities: BasePageable<VirtualAccountBankEntity?>): BasePageable<VirtualAccountBank> {
        return BasePageable(
            pageable = entities.pageable,
            content = entities.content.filterNotNull().map(this::entityToDomain)
        )
    }

}