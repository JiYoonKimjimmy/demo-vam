package com.konai.vam.core.common.error.handler

import com.konai.vam.core.common.EMPTY
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.ErrorResponse
import com.konai.vam.core.common.error.FeatureCode
import com.konai.vam.core.common.error.exception.BaseException
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.error.exception.RestClientServiceException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException

@RestControllerAdvice
class BaseExceptionHandler(
    private val featureCode: FeatureCode = FeatureCode.UNKNOWN
) {

    @ExceptionHandler(ResourceNotFoundException::class)
    protected fun handleResourceNotFoundException(e: BaseException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(featureCode, e.errorCode)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message = e.bindingResult.fieldErrors.joinToString(". ") { it.defaultMessage ?: EMPTY }
        return ErrorResponse.toResponseEntity(featureCode, ErrorCode.ARGUMENT_NOT_VALID_ERROR, message)
    }

    @ExceptionHandler(HttpClientErrorException::class)
    protected fun handleHttpClientErrorException(e: HttpClientErrorException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(featureCode, ErrorCode.EXTERNAL_SERVICE_ERROR, e.message)
    }

    @ExceptionHandler(RestClientServiceException::class)
    protected fun handleRestClientServiceException(e: RestClientServiceException): ResponseEntity<ErrorResponse> {
        return try {
            ErrorResponse.toResponseEntity(e.detailMessage!!)
        } catch (e: Exception) {
            ErrorResponse.toResponseEntity(featureCode, ErrorCode.EXTERNAL_SERVICE_ERROR, e.message)
        }
    }

    @ExceptionHandler(BaseException::class)
    protected fun handleBaseException(e: BaseException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(featureCode, e.errorCode, e.detailMessage)
    }

    @ExceptionHandler(Exception::class)
    protected fun exceptionHandler(e: Exception): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(featureCode, ErrorCode.UNKNOWN_ERROR, e.message)
    }

}