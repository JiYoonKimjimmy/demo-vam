package com.konai.vam.batch.v1.virtualaccount.batchhistory.service

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistoryMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.Result.SUCCESS
import com.konai.vam.core.repository.virtualaccountbatchhistory.VirtualAccountBatchHistoryEntityAdapter
import com.konai.vam.core.repository.virtualaccountbatchhistory.jdsl.VirtualAccountBatchHistoryPredicate
import org.springframework.stereotype.Service

@Service
class VirtualAccountBatchHistoryFindService(

    private val virtualAccountBatchHistoryMapper: VirtualAccountBatchHistoryMapper,
    private val virtualAccountBatchHistoryEntityAdapter: VirtualAccountBatchHistoryEntityAdapter

) : VirtualAccountBatchHistoryFindAdapter {

    override fun findByBatchId(batchId: String): VirtualAccountBatchHistory {
        return virtualAccountBatchHistoryEntityAdapter.findByPredicate(
                VirtualAccountBatchHistoryPredicate(cardSeBatchId = batchId, result = SUCCESS),
                pageableRequest = PageableRequest(0)
            )
            .orElseThrow { InternalServiceException(ErrorCode.VIRTUAL_ACCOUNT_BATCH_HISTORY_NOT_FOUND) }
            .let { virtualAccountBatchHistoryMapper.entityToDomain(it) }
    }

}