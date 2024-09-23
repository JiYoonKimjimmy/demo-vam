package fixtures

import com.konai.vam.api.v1.wooribank.controller.model.WooriBankManagementRequest
import com.konai.vam.core.common.WOORI_BANK_COMPANY_NO
import com.konai.vam.core.common.WOORI_BANK_IDENTIFIER_CODE
import com.konai.vam.core.common.WOORI_BANK_INSTITUTION_CODE
import com.konai.vam.core.enumerate.YesOrNo
import com.konai.vam.core.util.DATE_yyMMdd_PATTERN
import com.konai.vam.core.util.TIME_BASIC_PATTERN
import com.konai.vam.core.util.convertPatternOf
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class WooriBankManagementRequestFixture {

    fun make(
        messageTypeCode: String,
        businessTypeCode: String,
        messageNo: String,
        transmissionDate: String = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN),
        trDate: String = LocalDate.now().convertPatternOf(),
        trTime: String = LocalTime.now().convertPatternOf(),
        accountNo: String = "accountNo",
        trAmount: Int = 100000,
        orgMessageNo: String? = null
    ): WooriBankManagementRequest {
        return WooriBankManagementRequest(
            identifierCode = WOORI_BANK_IDENTIFIER_CODE,
            companyNo = WOORI_BANK_COMPANY_NO,
            institutionCode = WOORI_BANK_INSTITUTION_CODE,
            messageTypeCode = messageTypeCode,
            businessTypeCode = businessTypeCode,
            transmissionCount = 0,
            messageNo = messageNo,
            transmissionDate = transmissionDate,
            transmissionTime = LocalTime.now().convertPatternOf(TIME_BASIC_PATTERN),
            responseCode = null,
            orgMessageNo = orgMessageNo,
            parentAccountNo = generateUUID(14),
            trDate = trDate,
            trTime = trTime,
            trMedium = "01",
            trAmount = trAmount,
            otherCashierCheckAmount = 0,
            etcOtherCashierCheckAmount = 0,
            trBranch = "123456",
            depositorName = "123456",
            accountNo = accountNo,
            cashDepositYn = YesOrNo.Y.name,
            cashierCheckAmount = 0,
            branchCode = "1234567",
        )
    }

}