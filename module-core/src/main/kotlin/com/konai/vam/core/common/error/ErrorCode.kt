package com.konai.vam.core.common.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    var message: String
) {

    VIRTUAL_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "Virtual account not found"),
    SERVICE_ID_IS_INVALID(HttpStatus.BAD_REQUEST, "002", "This serviceId is invalid for connection virtual account"),
    BATCH_ID_ALREADY_CONNECTED(HttpStatus.INTERNAL_SERVER_ERROR, "003", "This batchId has already virtual account connected"),
    BATCH_ID_INVALID(HttpStatus.BAD_REQUEST, "004", "This batchId is invalid"),
    BATCH_FILE_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "005", "Batch file creation failed"),
    BATCH_FILE_PATH_NOT_FOUND(HttpStatus.NOT_FOUND, "006", "Batch file path not found"),
    BATCH_FILE_NOT_EXIST_IN_PATH(HttpStatus.INTERNAL_SERVER_ERROR, "007", "Batch file does not exist in path"),
    BATCH_FILE_CREATION_RESULT_IS_NOT_SUCCESSFUL(HttpStatus.INTERNAL_SERVER_ERROR, "008", "Batch file creation result is not successful"),
    SERVICE_POLICY_NOT_SUPPORT_FOR_FIXATION_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "009", "This serviceId is not a virtual account fixation connect type"),
    INSUFFICIENT_AVAILABLE_VIRTUAL_ACCOUNTS(HttpStatus.INTERNAL_SERVER_ERROR, "010", "Insufficient available virtual accounts"),
    VIRTUAL_ACCOUNT_HAS_NOT_CONNECTED_CARD(HttpStatus.INTERNAL_SERVER_ERROR, "011", "Virtual account has not connected card"),
    VIRTUAL_ACCOUNT_BANK_NOT_FOUND(HttpStatus.NOT_FOUND, "012", "Virtual account bank not found"),
    VIRTUAL_ACCOUNT_RECHARGE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "013", "Virtual account recharge failed"),
    VIRTUAL_ACCOUNT_RECHARGE_CANCEL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "014", "Virtual account recharge cancel failed"),
    RECHARGE_TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "015", "Recharge transaction not found"),
    WOORI_BANK_AGGREGATION_CACHE_NOT_FOUND(HttpStatus.NOT_FOUND, "016", "Woori bank aggregation cache not found"),
    VIRTUAL_ACCOUNT_BATCH_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "017", "Virtual account batch history not found"),
    WOORI_BANK_MESSAGE_CODE_INVALID(HttpStatus.BAD_REQUEST, "018", "Woori bank message code is invalid"),
    RECHARGE_TRANSACTION_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "019", "This recharge transaction has already been canceled"),
    RECHARGE_TRANSACTION_IS_INVALID(HttpStatus.BAD_REQUEST, "020", "This recharge transaction is invalid"),
    RECHARGE_AMOUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "021", "The recharge amount limit has been exceeded"),
    RECHARGE_CARD_STATUS_IS_NOT_ACTIVE(HttpStatus.BAD_REQUEST, "022", "The recharge card status is not active"),
    PARENT_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "023", "Parent account not found"),
    PARENT_ACCOUNT_IS_DUPLICATED(HttpStatus.BAD_REQUEST, "024", "Parent account is duplicated"),
    VIRTUAL_ACCOUNT_IS_DUPLICATED(HttpStatus.BAD_REQUEST, "025", "Virtual account is duplicated"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "900", "Internal server error"),
    ARGUMENT_NOT_VALID_ERROR(HttpStatus.BAD_REQUEST, "901", "Argument not valid"),
    EXTERNAL_URL_PROPERTY_NOT_DEFINED(HttpStatus.INTERNAL_SERVER_ERROR, "902", "External url property is not defined"),
    EXTERNAL_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "903", "External API service error"),
    EXTERNAL_SERVICE_RESPONSE_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "904", "External API response is null"),
    UNKNOWN_ENVIRONMENT(HttpStatus.INTERNAL_SERVER_ERROR, "905", "Unknown environment of project"),

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "999", "Unknown error"),

}