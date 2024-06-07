package com.konai.vam.core.common.error

import com.konai.vam.core.common.model.BaseResult
import org.springframework.http.ResponseEntity

class ErrorResponse(
    private val result: BaseResult = BaseResult()
) {

    fun getResult() = this.result

    companion object {

        fun toResponseEntity(featureCode: FeatureCode, errorCode: ErrorCode): ResponseEntity<ErrorResponse> {
            return ResponseEntity(ErrorResponse(result = BaseResult(featureCode, errorCode)), errorCode.status)
        }

        fun toResponseEntity(featureCode: FeatureCode, errorCode: ErrorCode, message: String?): ResponseEntity<ErrorResponse> {
            return BaseResult(featureCode, errorCode)
                .takeIf { message != null }
                ?.append(message)
                ?.let { ResponseEntity(ErrorResponse(result = it), errorCode.status) }
                ?: toResponseEntity(featureCode, errorCode)
        }

    }

}