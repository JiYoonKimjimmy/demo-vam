package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step

import com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step.item.VirtualAccountCardConnectItem
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step.item.VirtualAccountCardConnectItemMapper
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import org.springframework.batch.item.ItemProcessor

class VirtualAccountCardConnectItemProcessor(
    private val mapper: VirtualAccountCardConnectItemMapper,
    private val serviceId: String,
) : ItemProcessor<VirtualAccountEntity, VirtualAccountCardConnectItem> {

    override fun process(item: VirtualAccountEntity): VirtualAccountCardConnectItem {
        return mapper.entityToDomain(item, serviceId)
    }

}