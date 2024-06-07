package com.konai.vam.core.common.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "900", "Internal server error"),

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "999", "Unknown error"),

}
