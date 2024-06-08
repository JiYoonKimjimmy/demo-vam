package com.konai.vam.core.common.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "900", "Internal server error"),
    ARGUMENT_NOT_VALID_ERROR(HttpStatus.BAD_REQUEST, "901", "Argument not valid"),

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "999", "Unknown error"),

}
