package com.konai.vam.core.enumerate

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException

enum class WooriBankMessageType(
    val requestCode: Code,
    val responseCode: Code,
) {
    WORK_START                      (Code("0800", "100"), Code("0810", "100")),
    WORK_STOP                       (Code("0800", "400"), Code("0810", "400")),
    WORK_FAILURES                   (Code("0800", "999"), Code("0810", "999")),
    WORK_FAILURES_RESOLVED          (Code("0800", "888"), Code("0810", "888")),
    TRANSACTION_AGGREGATION         (Code("0700", "500"), Code("0710", "500")),
    VIRTUAL_ACCOUNT_INQUIRY         (Code("0200", "400"), Code("0210", "400")),
    VIRTUAL_ACCOUNT_DEPOSIT         (Code("0200", "600"), Code("0210", "600")),
    VIRTUAL_ACCOUNT_DEPOSIT_CANCEL  (Code("0420", "700"), Code("0430", "700")),
    VIRTUAL_ACCOUNT_DEPOSIT_CONFIRM (Code("0200", "800"), Code("0210", "800")),
    ;

    data class Code(
        val messageTypeCode: String,
        val businessTypeCode: String
    )

    companion object {
        fun find(messageTypeCode: String, businessTypeCode: String): WooriBankMessageType {
            val messageCode = Code(messageTypeCode, businessTypeCode)
            return entries.find { it.requestCode == messageCode || it.responseCode == messageCode } ?: throw InternalServiceException(ErrorCode.WOORI_BANK_MESSAGE_CODE_INVALID)
        }
    }

}