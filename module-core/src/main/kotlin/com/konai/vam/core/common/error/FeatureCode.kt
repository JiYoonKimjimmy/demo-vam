package com.konai.vam.core.common.error

enum class FeatureCode(
    val code: String,
    val message: String
) {

    UNKNOWN("9999", "Unknown Service"),

    VIRTUAL_ACCOUNT_SERVICE("1000", "Virtual Account Service"),
    VIRTUAL_ACCOUNT_BANK_SERVICE("1001", "Virtual Account Bank Service"),
    VIRTUAL_ACCOUNT_CARD_MANAGEMENT_SERVICE("1002", "Virtual Account Card Management Service"),

    WOORI_BANK_WORK_SERVICE("2000", "Woori Bank Work Service"),
    WOORI_BANK_MANAGEMENT_SERVICE("2001", "Woori Bank Management Service"),
    WOORI_BANK_AGGREGATION_SERVICE("2002", "Woori Bank Aggregation Service"),

}