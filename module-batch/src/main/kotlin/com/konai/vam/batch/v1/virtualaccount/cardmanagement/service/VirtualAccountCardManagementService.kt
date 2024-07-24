package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.VirtualAccountBatchHistorySaveAdapter
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.service.VirtualAccountBatchExecuteAdapter
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain.VirtualAccountCardConnect
import com.konai.vam.core.restclient.koditn.KodItnGetProductsBasicInfoRequest
import com.konai.vam.core.restclient.koditn.KodItnRestClient
import org.springframework.stereotype.Service

@Service
class VirtualAccountCardManagementService(

    private val virtualAccountCardConnectAdapter: VirtualAccountCardConnectAdapter,
    private val virtualAccountCardFetchAdapter: VirtualAccountCardFetchAdapter,
    private val virtualAccountBatchExecuteAdapter: VirtualAccountBatchExecuteAdapter,
    private val virtualAccountBatchHistorySaveAdapter: VirtualAccountBatchHistorySaveAdapter,
    private val kodItnRestClient: KodItnRestClient

) : VirtualAccountCardManagementAdapter {

    override fun connectBulkCard(batchId: String, serviceId: String) {
        val history = connectBulkCardToVirtualAccount(batchId, serviceId)
        createFileAndSaveBatchHistory(batchId, history)
    }

    override fun createBulkCardFile(batchId: String, batchHistory: VirtualAccountBatchHistory): String {
        return createFileAndSaveBatchHistory(batchId, batchHistory)
    }

    private fun connectBulkCardToVirtualAccount(batchId: String, serviceId: String): VirtualAccountBatchHistory {
        // 고정 매핑을 지원하는지 판단한다.
        val bankCode = fetchVirtualAccountBankCode(serviceId)
        // 배치 아이디로 발급된 실물카드를 조회한다.
        val pars = fetchParListMatchingBatchId(batchId)
        // 실물카드를 가상계좌에 매핑하고, 메타 정보들을 저장한다.
        return virtualAccountCardConnectAdapter.connectCardToVirtualAccounts(VirtualAccountCardConnect(batchId, serviceId, bankCode, pars))
    }

    fun fetchVirtualAccountBankCode(serviceId: String): String {
        return kodItnRestClient.getProductsBasicInfo(KodItnGetProductsBasicInfoRequest(serviceId))
            .checkFixableVirtualAccountPolicy()
            .virtualAccountBankCode!!
    }

    fun fetchParListMatchingBatchId(batchId: String): List<String> {
        return virtualAccountCardFetchAdapter.fetchParsByBatchId(batchId)
    }

    private fun createFileAndSaveBatchHistory(batchId: String, batchHistory: VirtualAccountBatchHistory): String {
        val filePath = createSemFile(batchId, batchHistory)
        // TODO("파일 생성 실패인 경우. `FAILED` 상태로 Batch 이력 저장 처리")
        val updatedHistory = batchHistory.updateFilePathOnSuccess(filePath)
        return saveBatchHistory(updatedHistory)
    }

    private fun createSemFile(batchId: String, batchHistory: VirtualAccountBatchHistory): String {
        return virtualAccountBatchExecuteAdapter.executeCreateSemFileBatchJob(batchId, batchHistory)
    }

    private fun saveBatchHistory(batchHistory: VirtualAccountBatchHistory): String {
        return virtualAccountBatchHistorySaveAdapter.save(batchHistory).filePath!!
    }

}