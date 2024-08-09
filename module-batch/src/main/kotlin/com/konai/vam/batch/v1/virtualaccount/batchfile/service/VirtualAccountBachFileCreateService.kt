package com.konai.vam.batch.v1.virtualaccount.batchfile.service

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.VirtualAccountBatchHistorySaveAdapter
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.service.VirtualAccountBatchExecuteAdapter
import org.springframework.stereotype.Service

@Service
class VirtualAccountBachFileCreateService(
    private val virtualAccountBatchExecuteAdapter: VirtualAccountBatchExecuteAdapter,
    private val virtualAccountBatchHistorySaveAdapter: VirtualAccountBatchHistorySaveAdapter,
) : VirtualAccountBachFileCreateAdapter {

    override fun createBatchFile(batchId: String, batchHistory: VirtualAccountBatchHistory): String {
        return createSemFile(batchId, batchHistory.serviceId, batchHistory.count)
            .let { batchHistory.update(it) }
            .let { saveBatchHistory(it) }
    }

    private fun createSemFile(batchId: String, serviceId: String, quantity: Int): String {
        return virtualAccountBatchExecuteAdapter.executeCreateSemFileBatchJob(batchId, serviceId, quantity)
    }

    private fun saveBatchHistory(batchHistory: VirtualAccountBatchHistory): String {
        return virtualAccountBatchHistorySaveAdapter.save(batchHistory).filePath!!
    }

}