package com.konai.vam.core.common.model.wooribank

import com.konai.vam.core.common.EMPTY
import com.konai.vam.core.common.WOORI_BANK_COMPANY_NO
import com.konai.vam.core.common.WOORI_BANK_IDENTIFIER_CODE
import com.konai.vam.core.common.WOORI_BANK_INSTITUTION_CODE
import com.konai.vam.core.enumerate.WooriBankMessage
import com.konai.vam.core.util.convertPatternOf
import java.time.LocalDate
import java.time.LocalTime

data class WooriBankCommonModel(
    val identifierCode: String,
    val companyNo : String,
    val institutionCode : String,
    val messageTypeCode: String,
    val businessTypeCode: String,
    val transmissionCount: Int,
    val messageNo: String,
    val transmissionDate: String,
    val transmissionTime: String,
    val responseCode: String,
    val orgMessageNo: String,
) {
    constructor(messageCode: WooriBankMessage.WooriBankMessageCode): this(
        identifierCode = WOORI_BANK_IDENTIFIER_CODE,
        companyNo = WOORI_BANK_COMPANY_NO,
        institutionCode = WOORI_BANK_INSTITUTION_CODE,
        messageTypeCode = messageCode.messageTypeCode,
        businessTypeCode = messageCode.businessTypeCode,
        transmissionCount = 0,
        messageNo = EMPTY,
        transmissionDate = LocalDate.now().convertPatternOf("yyMMdd"),
        transmissionTime = LocalTime.now().convertPatternOf("HHmmss"),
        responseCode = EMPTY,
        orgMessageNo = EMPTY,
    )
}