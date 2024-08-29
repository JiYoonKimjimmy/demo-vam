package fixtures

import com.konai.vam.core.common.*
import com.konai.vam.core.common.model.wooribank.WooriBankCommonMessage
import com.konai.vam.core.enumerate.WooriBankMessage
import com.konai.vam.core.util.DATE_yyMMdd_PATTERN
import com.konai.vam.core.util.TIME_BASIC_PATTERN
import com.konai.vam.core.util.convertPatternOf
import java.time.LocalDate
import java.time.LocalTime

class WooriBankCommonMessageFixture {

    fun make(
        messageCode: WooriBankMessage.WooriBankMessageCode,
        messageNo: String,
    ): WooriBankCommonMessage {
        return WooriBankCommonMessage(
            identifierCode = WOORI_BANK_IDENTIFIER_CODE,
            companyNo = WOORI_BANK_COMPANY_NO,
            institutionCode = WOORI_BANK_INSTITUTION_CODE,
            messageTypeCode = messageCode.messageTypeCode,
            businessTypeCode = messageCode.businessTypeCode,
            transmissionCount = ZERO_STR,
            messageNo = messageNo,
            transmissionDate = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN),
            transmissionTime = LocalTime.now().convertPatternOf(TIME_BASIC_PATTERN),
            responseCode = EMPTY,
            orgMessageNo = EMPTY,
        )
    }

}