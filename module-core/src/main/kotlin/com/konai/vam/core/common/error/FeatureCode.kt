package com.konai.vam.core.common.error

enum class FeatureCode(
    val code: String,
    val message: String
) {

    UNKNOWN("9999", "Unknown Service"),

    VIRTUAL_ACCOUNT_SERVICE("1000", "Virtual Account Service"),

}