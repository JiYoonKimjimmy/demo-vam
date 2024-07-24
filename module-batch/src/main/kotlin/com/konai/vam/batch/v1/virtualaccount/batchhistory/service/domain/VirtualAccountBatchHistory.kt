package com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.enumerate.Result.SUCCESS

data class VirtualAccountBatchHistory(
    val id: Long? = null,
    val cardSeBatchId: String,
    val serviceId: String,
    val count: Int,
    val result: Result?,
    val reason: String? = null,
    var filePath: String? = null,
) {

    fun checkResultIsSuccessed(): VirtualAccountBatchHistory {
        if (this.result == Result.FAILED) throw InternalServiceException(ErrorCode.BATCH_FILE_CREATION_RESULT_IS_NOT_SUCCESSFUL)
        return this
    }

    fun clearIdAndFilePath(): VirtualAccountBatchHistory {
        return copy(id = null, filePath = null)
    }

    fun updateFilePathOnSuccess(filePath: String): VirtualAccountBatchHistory {
        if (result == SUCCESS) this.filePath = filePath
        return this
    }

}