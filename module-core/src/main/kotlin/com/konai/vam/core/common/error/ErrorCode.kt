package com.konai.vam.core.common.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    var message: String
) {

    VIRTUAL_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "Virtual account not found"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "900", "Internal server error"),
    ARGUMENT_NOT_VALID_ERROR(HttpStatus.BAD_REQUEST, "901", "Argument not valid"),
    EXTERNAL_URL_PROPERTY_NOT_DEFINED(HttpStatus.INTERNAL_SERVER_ERROR, "902", "External url property is not defined"),
    EXTERNAL_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "903", "External API service error"),
    EXTERNAL_SERVICE_RESPONSE_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "904", "External API response is null"),

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "999", "Unknown error"),

}