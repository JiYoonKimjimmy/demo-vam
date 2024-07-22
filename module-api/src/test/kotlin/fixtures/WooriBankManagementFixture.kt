package fixtures

import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagement
import com.konai.vam.core.common.WOORI_BANK_COMPANY_NO
import com.konai.vam.core.common.WOORI_BANK_IDENTIFIER_CODE
import com.konai.vam.core.common.WOORI_BANK_INSTITUTION_CODE
import com.konai.vam.core.enumerate.YesOrNo
import com.konai.vam.core.util.convertPatternOf
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class WooriBankManagementFixture {

    fun make(
        messageTypeCode: String,
        businessTypeCode: String,
        messageNo: String,
        transmissionDate: String = LocalDate.now().convertPatternOf("yyMMdd"),
        transmissionTime: String = LocalTime.now().convertPatternOf("HHmmss"),
        accountNo: String = "accountNo",
        trAmount: Int = 100000,
        orgMessageNo: String? = null
    ): WooriBankManagement {
        return WooriBankManagement(
            identifierCode = WOORI_BANK_IDENTIFIER_CODE,
            companyNo = WOORI_BANK_COMPANY_NO,
            institutionCode = WOORI_BANK_INSTITUTION_CODE,
            messageTypeCode = messageTypeCode,
            businessTypeCode = businessTypeCode,
            transmissionCount = 0,
            messageNo = messageNo,
            transmissionDate = transmissionDate,
            transmissionTime = transmissionTime,
            responseCode = null,
            orgMessageNo = orgMessageNo,
            parentAccount = "parentAccount",
            trDate = transmissionDate,
            trTime = transmissionTime,
            tid = UUID.randomUUID().toString(),
            trMedium = "01",
            trAmount = trAmount,
            otherCashierCheckAmount = trAmount,
            etcOtherCashierCheckAmount = trAmount,
            trBranch = "trBranch",
            depositorName = "depositorName",
            accountNo = accountNo,
            cashDepositYn = YesOrNo.Y.name,
            cashierCheckAmount = trAmount,
            branchCode = "branchCode",
        )
    }

}