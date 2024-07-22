package com.konai.vam.api.v1.wooribank.service.common

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.BaseException
import com.konai.vam.core.enumerate.WooriBankResponseCode
import org.slf4j.MDC
import java.lang.Exception

class WooriBankErrorResponse(
    val responseCode: WooriBankResponseCode,
    val responseMessage: String
) {

    companion object {
        fun of(exception: Exception): WooriBankErrorResponse {
            return when (exception) {
                is BaseException -> {
                    WooriBankErrorResponse(
                        responseCode = WooriBankResponseCode.of(exception.errorCode),
                        responseMessage = exception.errorCode.message
                    )
                }
                else -> {
                    WooriBankErrorResponse(
                        responseCode = WooriBankResponseCode.K999,
                        responseMessage = "${ErrorCode.UNKNOWN_ERROR.message}. [${MDC.get("correlationId")}]"
                    )
                }
            }
        }
    }

}