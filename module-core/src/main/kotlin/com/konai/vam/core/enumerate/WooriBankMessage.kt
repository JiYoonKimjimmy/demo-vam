package com.konai.vam.core.enumerate

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException

enum class WooriBankMessage(
    val requestCode: WooriBankMessageCode,
    val responseCode: WooriBankMessageCode,
) {
    WORK_START                      (WooriBankMessageCode("0800", "100"), WooriBankMessageCode("0810", "100")),
    WORK_STOP                       (WooriBankMessageCode("0800", "400"), WooriBankMessageCode("0810", "400")),
    WORK_FAILURES                   (WooriBankMessageCode("0800", "999"), WooriBankMessageCode("0810", "999")),
    WORK_FAILURES_RESOLVED          (WooriBankMessageCode("0800", "888"), WooriBankMessageCode("0810", "888")),
    TRANSACITON_AGGREGATION         (WooriBankMessageCode("0700", "500"), WooriBankMessageCode("0710", "500")),
    VIRTUAL_ACCOUNT_INQUIRY         (WooriBankMessageCode("0200", "400"), WooriBankMessageCode("0210", "400")),
    VIRTUAL_ACCOUNT_DEPOSIT         (WooriBankMessageCode("0200", "600"), WooriBankMessageCode("0210", "600")),
    VIRTUAL_ACCOUNT_DEPOSIT_CANCEL  (WooriBankMessageCode("0420", "700"), WooriBankMessageCode("0430", "700")),
    VIRTUAL_ACCOUNT_DEPOSIT_CONFIRM (WooriBankMessageCode("0200", "800"), WooriBankMessageCode("0210", "800")),
    ;

    data class WooriBankMessageCode(
        val messageTypeCode: String,
        val businessTypeCode: String
    )

    companion object {
        fun find(messageTypeCode: String, businessTypeCode: String): WooriBankMessage {
            val messageCode = WooriBankMessageCode(messageTypeCode, businessTypeCode)
            return entries.find { it.requestCode == messageCode || it.responseCode == messageCode } ?: throw InternalServiceException(ErrorCode.WOORI_BANK_MESSAGE_CODE_INVALID)
        }
    }

}