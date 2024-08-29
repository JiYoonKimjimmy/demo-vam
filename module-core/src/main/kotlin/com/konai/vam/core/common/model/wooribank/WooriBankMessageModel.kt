package com.konai.vam.core.common.model.wooribank

import com.konai.vam.core.common.*
import com.konai.vam.core.util.DATE_yyMMdd_PATTERN
import com.konai.vam.core.util.TIME_BASIC_PATTERN
import com.konai.vam.core.util.convertPatternOf
import java.time.LocalDate
import java.time.LocalTime

sealed class WooriBankCommonMessage(
    open val identifierCode: String = WOORI_BANK_IDENTIFIER_CODE,
    open val companyNo : String = WOORI_BANK_COMPANY_NO,
    open val institutionCode : String = WOORI_BANK_INSTITUTION_CODE,
    open val messageTypeCode: String,
    open val businessTypeCode: String,
    open val transmissionCount: String = ZERO_STR,
    open val messageNo: String,
    open val transmissionDate: String = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN),
    open val transmissionTime: String = LocalTime.now().convertPatternOf(TIME_BASIC_PATTERN),
    open val responseCode: String = EMPTY,
    open val orgMessageNo: String = EMPTY,
)

data class WooriBankMessage(
    override val messageTypeCode: String,
    override val businessTypeCode: String,
    override val messageNo: String,
) : WooriBankCommonMessage(
    messageTypeCode = messageTypeCode,
    businessTypeCode = businessTypeCode,
    messageNo = messageNo
)

data class WooriBankAggregationMessage(
    override val messageTypeCode: String,
    override val businessTypeCode: String,
    override val messageNo: String,
    val aggregationDate: String,
    val konaDepositCount: Int,
    val konaDepositAmount: Long,
    val konaDepositCancelCount: Int,
    val konaDepositCancelAmount: Long,
    val konaDepositTrAmount: Long,
    val bankDepositCount: Int = 0,
    val bankDepositAmount: Long = 0,
    val bankDepositCancelCount: Int = 0,
    val bankDepositCancelAmount: Long = 0,
    val bankDepositTrAmount: Long = 0
) : WooriBankCommonMessage(
    messageTypeCode = messageTypeCode,
    businessTypeCode = businessTypeCode,
    messageNo = messageNo
)