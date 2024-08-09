package com.konai.vam.batch.v1.virtualaccount.batchfile.service

import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountFindAdapter
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.VirtualAccountBatchHistoryFindAdapter
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory
import com.konai.vam.batch.v1.virtualaccount.batchfile.service.domain.VirtualAccountBatchFile
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.CONNECTED
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class VirtualAccountBatchFileDownloadService(

    private val virtualAccountFindAdapter: VirtualAccountFindAdapter,
    private val virtualAccountBachFileCreateAdapter: VirtualAccountBachFileCreateAdapter,
    private val virtualAccountBatchHistoryFindAdapter: VirtualAccountBatchHistoryFindAdapter

) : VirtualAccountBatchFileDownloadAdapter {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun downloadBulkCardFile(batchId: String): VirtualAccountBatchFile {
        val batchHistory = findBatchHistory(batchId)
        return try {
            VirtualAccountBatchFile(batchHistory.checkIsExistsFile().filePath!!)
        } catch (e: ResourceNotFoundException) {
            // 전문 파일 없는 경우, 재생성 처리
            logger.error(e.stackTraceToString())
            handlingMissingFile(batchId, batchHistory)
        }
    }

    private fun findBatchHistory(batchId: String): VirtualAccountBatchHistory {
        return virtualAccountBatchHistoryFindAdapter.findByBatchId(batchId)
            .checkIsSuccessResult()
    }

    private fun handlingMissingFile(batchId: String, batchHistory: VirtualAccountBatchHistory): VirtualAccountBatchFile {
        return VirtualAccountBatchFile(filePath = reCreateBulkCardFile(batchId, batchHistory))
    }

    private fun reCreateBulkCardFile(batchId: String, batchHistory: VirtualAccountBatchHistory): String {
        // 실물 카드 연결 완료 가상 계좌 존재 여부 확인
        checkExistsVirtualAccountConnected(batchId)
        // batchHistory 초기화하여 전문 연동 파일 생성 요청
        return virtualAccountBachFileCreateAdapter.createBatchFile(batchId, batchHistory.clear())
    }

    private fun checkExistsVirtualAccountConnected(batchId: String) {
        if (!virtualAccountFindAdapter.existsByConnectStatusAndBatchId(CONNECTED, batchId)) {
            // 요청 `batchId` 기준 `CONNECTED` 연결 상태 가상 계좌 없는 경우, 예외 처리
            throw ResourceNotFoundException(ErrorCode.BATCH_ID_INVALID)
        }
    }

}