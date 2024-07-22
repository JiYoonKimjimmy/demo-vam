package com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain

import com.konai.vam.core.repository.virtualaccountbatchhistory.entity.VirtualAccountBatchHistoryEntity
import org.springframework.stereotype.Component

@Component
class VirtualAccountBatchHistoryMapper {

    fun entityToDomain(entity: VirtualAccountBatchHistoryEntity): VirtualAccountBatchHistory {
        return VirtualAccountBatchHistory(
            id = entity.id!!,
            cardSeBatchId = entity.cardSeBatchId,
            serviceId = entity.serviceId,
            count = entity.count,
            result = entity.result,
            reason = entity.reason,
            filePath = entity.filePath
        )
    }

    fun domainToEntity(domain: VirtualAccountBatchHistory): VirtualAccountBatchHistoryEntity {
        return VirtualAccountBatchHistoryEntity(
            id = domain.id,
            cardSeBatchId = domain.cardSeBatchId,
            serviceId = domain.serviceId,
            count = domain.count,
            result = domain.result,
            reason = domain.reason,
            filePath = domain.filePath
        )
    }

}