package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistoryMapper
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain.VirtualAccountCardFile
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.Result.SUCCESS
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.CONNECTED
import com.konai.vam.core.repository.virtualaccount.VirtualAccountEntityAdapter
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import com.konai.vam.core.repository.virtualaccountbatchhistory.VirtualAccountBatchHistoryEntityAdapter
import com.konai.vam.core.repository.virtualaccountbatchhistory.jdsl.VirtualAccountBatchHistoryPredicate
import org.slf4j.LoggerFactory
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service
class VirtualAccountCardFileDownloadService(

    private val virtualAccountEntityAdapter: VirtualAccountEntityAdapter,
    private val virtualAccountBatchHistoryEntityAdapter: VirtualAccountBatchHistoryEntityAdapter,
    private val virtualAccountBatchHistoryMapper: VirtualAccountBatchHistoryMapper,

    private val virtualAccountCardManagementAdapter: VirtualAccountCardManagementAdapter

) : VirtualAccountCardFileDownloadAdapter {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun downloadBulkCardFile(batchId: String): VirtualAccountCardFile {
        val batchHistory = findBatchHistoryMatchingBatchId(batchId).checkResultIsSuccessed()
        val filePath = batchHistory.filePath ?: throw ResourceNotFoundException(ErrorCode.BATCH_FILE_PATH_NOT_FOUND)
        return try {
            checkFileExist(filePath)
            generateBatchFileResource(filePath)
        } catch (e: ResourceNotFoundException) {
            logger.error(e.stackTraceToString())
            handlingMissingFile(batchId, batchHistory)
        }
    }

    private fun findBatchHistoryMatchingBatchId(batchId: String): VirtualAccountBatchHistory {
        return virtualAccountBatchHistoryEntityAdapter.findByPredicate(
                VirtualAccountBatchHistoryPredicate(cardSeBatchId = batchId, result = SUCCESS),
                pageableRequest = PageableRequest(0)
            )
            .orElseThrow { InternalServiceException(ErrorCode.VIRTUAL_ACCOUNT_BATCH_HISTORY_NOT_FOUND) }
            .let { virtualAccountBatchHistoryMapper.entityToDomain(it) }
    }

    private fun checkFileExist(filePath: String) {
        if (!Files.exists(Paths.get(filePath))) {
            throw ResourceNotFoundException(ErrorCode.BATCH_FILE_NOT_EXIST_IN_PATH)
        }
    }

    private fun handlingMissingFile(batchId: String, batchHistory: VirtualAccountBatchHistory): VirtualAccountCardFile {
        return if (isAlreadyCardConnected(batchId)) {
            // 요청 `batchId` 기준 카드 연결 완료 가상 계좌 정보가 있는 경우
            generateBatchFileResource(filePath = createAndEncryptSemFile(batchId, batchHistory))
        } else {
            throw ResourceNotFoundException(ErrorCode.BATCH_ID_INVALID)
        }
    }

    private fun isAlreadyCardConnected(batchId: String): Boolean {
        val connectedVirtualAccounts = virtualAccountEntityAdapter.findAllByPredicate(
            VirtualAccountPredicate(cardSeBatchId = batchId, cardConnectStatus = CONNECTED),
            pageableRequest = PageableRequest(number = 0)
        )
        return connectedVirtualAccounts.content.isNotEmpty()
    }

    private fun createAndEncryptSemFile(batchId: String, batchHistory: VirtualAccountBatchHistory): String {
        return virtualAccountCardManagementAdapter.createBulkCardFile(batchId, batchHistory.clearIdAndFilePath())
    }

    private fun generateBatchFileResource(filePath: String): VirtualAccountCardFile {
        return VirtualAccountCardFile(filePath, UrlResource(Paths.get(filePath).toUri()))
    }

}