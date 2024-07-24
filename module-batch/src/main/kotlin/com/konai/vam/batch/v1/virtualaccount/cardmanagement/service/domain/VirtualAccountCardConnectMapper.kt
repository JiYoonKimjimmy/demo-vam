package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain

import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.BaseException
import com.konai.vam.core.enumerate.Result
import org.springframework.stereotype.Component

@Component
class VirtualAccountCardConnectMapper {

    fun domainToHistory(domain: VirtualAccountCardConnect, result: Result, exception: Exception?): VirtualAccountBatchHistory {
        val exceptionMessage: (exception: Exception) -> String = {
            when (it) {
                is BaseException -> it.errorCode.message
                else -> it.message ?: ErrorCode.UNKNOWN_ERROR.message
            }
        }

        return VirtualAccountBatchHistory(
            cardSeBatchId = domain.batchId,
            serviceId = domain.serviceId,
            count = domain.pars.size,
            result = result,
            reason = exception?.let(exceptionMessage)
        )
    }

}