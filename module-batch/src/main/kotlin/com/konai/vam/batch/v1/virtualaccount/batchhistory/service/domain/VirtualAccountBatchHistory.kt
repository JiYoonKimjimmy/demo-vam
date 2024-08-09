package com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.enumerate.Result.SUCCESS
import java.nio.file.Files
import java.nio.file.Paths

data class VirtualAccountBatchHistory(
    val id: Long? = null,
    val cardSeBatchId: String,
    val serviceId: String,
    val count: Int,
    val result: Result?,
    val reason: String? = null,
    var filePath: String? = null,
) {

    fun checkIsSuccessResult(): VirtualAccountBatchHistory {
        if (this.result == Result.FAILED) throw InternalServiceException(ErrorCode.BATCH_FILE_CREATION_RESULT_IS_NOT_SUCCESSFUL)
        return this
    }

    fun checkIsExistsFile(): VirtualAccountBatchHistory {
        if (!Files.exists(Paths.get(this.filePath!!))) {
            throw ResourceNotFoundException(ErrorCode.BATCH_FILE_NOT_EXIST_IN_PATH)
        }
        return this
    }

    fun clear(): VirtualAccountBatchHistory {
        return copy(id = null, filePath = null)
    }

    fun update(filePath: String): VirtualAccountBatchHistory {
        return copy(
            filePath = if (this.result == SUCCESS) filePath else null,
        )
    }

}