package com.konai.vam.batch.v1.virtualaccount.batchhistory.service

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistoryMapper
import com.konai.vam.core.repository.virtualaccountbatchhistory.VirtualAccountBatchHistoryEntityAdapter
import org.springframework.stereotype.Service

@Service
class VirtualAccountBatchHistorySaveService(
    private val virtualAccountBatchHistoryMapper: VirtualAccountBatchHistoryMapper,
    private val virtualAccountBatchHistoryEntityAdapter: VirtualAccountBatchHistoryEntityAdapter
) : VirtualAccountBatchHistorySaveAdapter {

    override fun save(domain: VirtualAccountBatchHistory): VirtualAccountBatchHistory {
        return virtualAccountBatchHistoryMapper.domainToEntity(domain)
            .let { virtualAccountBatchHistoryEntityAdapter.save(it) }
            .let { virtualAccountBatchHistoryMapper.entityToDomain(it) }
    }

    override fun saveAndFlush(domain: VirtualAccountBatchHistory): VirtualAccountBatchHistory {
        return virtualAccountBatchHistoryMapper.domainToEntity(domain)
            .let { virtualAccountBatchHistoryEntityAdapter.saveAndFlush(it) }
            .let { virtualAccountBatchHistoryMapper.entityToDomain(it) }
    }

}