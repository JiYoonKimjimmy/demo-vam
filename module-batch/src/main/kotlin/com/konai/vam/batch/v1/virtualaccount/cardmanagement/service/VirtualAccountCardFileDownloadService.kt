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
        val batchHistory = fetchBatchHistoryMatchingBatchId(batchId)
        val filePath = batchHistory.filePath ?: throw ResourceNotFoundException(ErrorCode.FILE_PATH_NOT_FOUND)
        try {
            checkFileExist(filePath)
        } catch (e: ResourceNotFoundException) {
            logger.error(e.stackTraceToString())
            handlingMissingFile(batchId, batchHistory)
        }
        return generateBatchFileResource(filePath)
    }

    private fun fetchBatchHistoryMatchingBatchId(batchId: String): VirtualAccountBatchHistory {
        return virtualAccountBatchHistoryEntityAdapter.findByPredicate(
            VirtualAccountBatchHistoryPredicate(cardSeBatchId = batchId, result = SUCCESS),
            pageableRequest = PageableRequest(0)
        )
            .orElseThrow { InternalServiceException(ErrorCode.VIRTUAL_ACCOUNT_BATCH_HISTORY_NOT_FOUND) }
            .let { virtualAccountBatchHistoryMapper.entityToDomain(it) }
    }

    private fun checkFileExist(filePath: String) {
        if (!Files.exists(Paths.get(filePath))) {
            throw ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND_IN_PATH)
        }
    }

    private fun handlingMissingFile(batchId: String, batchHistory: VirtualAccountBatchHistory): VirtualAccountCardFile {
        // 전문생성 전, 필요 조건인 카드와 계좌의 매핑 여부를 판단한다.
        return if (isCardMapped(batchId)) {
            generateBatchFileResource(filePath = createAndEncryptSemFile(batchId, batchHistory))
        } else {
            throw ResourceNotFoundException(ErrorCode.FAIL_TO_CREATE_BATCH_FILE)
        }
    }

    private fun isCardMapped(batchId: String): Boolean {
        val mappedVirtualAccounts = virtualAccountEntityAdapter.findAllByPredicate(
            VirtualAccountPredicate(cardSeBatchId = batchId, cardConnectStatus = CONNECTED),
            pageableRequest = PageableRequest(number = 0)
        )
        return mappedVirtualAccounts.content.isNotEmpty()
    }

    private fun createAndEncryptSemFile(batchId: String, batchHistory: VirtualAccountBatchHistory): String {
        return virtualAccountCardManagementAdapter.createBulkCardFile(batchId, batchHistory.clearIdAndFilePath())
    }

    private fun generateBatchFileResource(filePath: String): VirtualAccountCardFile {
        return VirtualAccountCardFile(filePath, UrlResource(Paths.get(filePath).toUri()))
    }

}