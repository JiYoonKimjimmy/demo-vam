package com.konai.vam.core.common.error

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.konai.vam.core.common.model.BaseResult
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ErrorResponse(
    val result: BaseResult = BaseResult()
) {

    companion object {

        fun toResponseEntity(featureCode: FeatureCode, errorCode: ErrorCode, detailMessage: String? = null): ResponseEntity<ErrorResponse> {
            return ResponseEntity(ErrorResponse(result = BaseResult(featureCode, errorCode, detailMessage)), errorCode.status)
        }

        fun toResponseEntity(detailMessage: String): ResponseEntity<ErrorResponse> {
            val httpStatus = HttpStatus.valueOf(detailMessage.substringBefore(" : ").toInt())
            val errorResponse = jacksonObjectMapper().readValue(detailMessage.substringAfter(" : ").trim('"'), ErrorResponse::class.java)
            return ResponseEntity(ErrorResponse(result = errorResponse.result), httpStatus)
        }

    }

}