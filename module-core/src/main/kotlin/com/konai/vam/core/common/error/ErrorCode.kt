package com.konai.vam.core.common.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    var message: String
) {

    VIRTUAL_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "Virtual account not found"),
    SERVICE_ID_IS_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "002", "This serviceId is invalid for connection virtual account"),
    BATCH_ID_ALREADY_CONNECTED(HttpStatus.INTERNAL_SERVER_ERROR, "003", "This batchId has already bean connected"),
    BATCH_ID_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "004", "This batchId is invalid"),
    FAIL_TO_CREATE_BATCH_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "005", "Failed to create batch file"),
    NO_FILE_CREATION_RESULTS(HttpStatus.INTERNAL_SERVER_ERROR, "006", "No file creation results matching batchId"),
    FILE_NOT_FOUND_IN_PATH(HttpStatus.INTERNAL_SERVER_ERROR, "007", "The file matching batchId does not exist in the path"),
    FILE_CREATION_NOT_SUCCESSFUL(HttpStatus.INTERNAL_SERVER_ERROR, "008", "The file matching batchId does not result in success"),
    PRODUCT_NOT_SUPPORT_FOR_FIXATION_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "009", "This product is not support for fixation virtual account"),
    INSUFFICIENT_VIRTUAL_ACCOUNTS(HttpStatus.SERVICE_UNAVAILABLE, "010", "Insufficient number of virtual accounts available"),
    VIRTUAL_ACCOUNT_HAS_NOT_CONNECTED_CARD(HttpStatus.NOT_FOUND, "011", "Virtual account has not connected card"),
    VIRTUAL_ACCOUNT_BANK_NOT_FOUND(HttpStatus.NOT_FOUND, "012", "Virtual account bank not found"),
    VIRTUAL_ACCOUNT_RECHARGE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "013", "Virtual account recharge failed"),
    VIRTUAL_ACCOUNT_RECHARGE_CANCEL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "014", "Virtual account recharge cancel failed"),
    RECHARGE_TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "015", "Recharge transaction not found"),
    WOORI_BANK_AGGREGATION_CACHE_NOT_FOUND(HttpStatus.NOT_FOUND, "016", "Woori bank aggregation cache not found"),
    VIRTUAL_ACCOUNT_BATCH_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "017", "Virtual account batch history not found"),
    FILE_PATH_NOT_FOUND(HttpStatus.NOT_FOUND, "018", "File path not found"),
    WOORI_BANK_MESSAGE_CODE_INVALID(HttpStatus.NOT_FOUND, "019", "Woori bank message code is invalid"),
    RECHARGE_TRANSACTION_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "020", "This recharge transaction has already been canceled"),
    RECHARGE_TRANSACTION_IS_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "021", "This recharge transaction is invalid"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "900", "Internal server error"),
    ARGUMENT_NOT_VALID_ERROR(HttpStatus.BAD_REQUEST, "901", "Argument not valid"),
    EXTERNAL_URL_PROPERTY_NOT_DEFINED(HttpStatus.INTERNAL_SERVER_ERROR, "902", "External url property is not defined"),
    EXTERNAL_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "903", "External API service error"),
    EXTERNAL_SERVICE_RESPONSE_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "904", "External API response is null"),

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "999", "Unknown error"),

}