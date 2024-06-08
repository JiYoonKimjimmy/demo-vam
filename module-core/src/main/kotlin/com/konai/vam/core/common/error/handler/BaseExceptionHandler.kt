package com.konai.vam.core.common.error.handler

import com.konai.vam.core.common.EMPTY
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.ErrorResponse
import com.konai.vam.core.common.error.FeatureCode
import com.konai.vam.core.common.error.exception.BaseException
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BaseExceptionHandler(
    private val featureCode: FeatureCode = FeatureCode.UNKNOWN
) {

    @ExceptionHandler(value = [
        ResourceNotFoundException::class
    ])
    protected fun resourceNotFoundExceptionHandler(e: BaseException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(featureCode, e.errorCode)
    }

    @ExceptionHandler(BaseException::class)
    protected fun handleCustomException(e: BaseException): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(featureCode, e.errorCode)
    }

    @ExceptionHandler(Exception::class)
    protected fun exceptionHandler(e: Exception): ResponseEntity<ErrorResponse> {
        return ErrorResponse.toResponseEntity(featureCode, ErrorCode.UNKNOWN_ERROR, e.message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message = e.bindingResult.fieldErrors.joinToString(". ") { it.defaultMessage ?: EMPTY }
        return ErrorResponse.toResponseEntity(featureCode, ErrorCode.ARGUMENT_NOT_VALID_ERROR, message)
    }

}