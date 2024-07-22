package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step.item

import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import org.springframework.stereotype.Component

@Component
class VirtualAccountCardConnectItemMapper {

    fun entityToDomain(entity: VirtualAccountEntity, serviceId: String): VirtualAccountCardConnectItem {
        return VirtualAccountCardConnectItem(
            id = entity.id!!,
            serviceId = serviceId,
            accountNo = entity.accountNo,
            par = entity.par!!,
        )
    }

}