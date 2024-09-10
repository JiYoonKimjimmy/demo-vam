package fixtures

import com.konai.vam.api.v1.wooribank.service.transaction.domain.WooriBankTransaction
import com.konai.vam.core.common.WOORI_BANK_COMPANY_NO
import com.konai.vam.core.common.WOORI_BANK_IDENTIFIER_CODE
import com.konai.vam.core.common.WOORI_BANK_INSTITUTION_CODE
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.enumerate.WooriBankResponseCode
import com.konai.vam.core.util.DATE_yyMMdd_PATTERN
import com.konai.vam.core.util.TIME_BASIC_PATTERN
import com.konai.vam.core.util.convertPatternOf
import java.time.LocalDate
import java.time.LocalTime

class WooriBankTransactionFixture {

    fun make(
        id: Long? = null,
        messageType: WooriBankMessageType = WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT,
        messageNo: String,
        orgMessageNo: String? = null,
        accountNo: String = "1234567890",
        trAmount: Long = 100000,
        trDate: String = LocalDate.now().convertPatternOf(),
        trTime: String = LocalTime.now().convertPatternOf(),
        responseCode: WooriBankResponseCode? = null
    ): WooriBankTransaction {
        return WooriBankTransaction(
            id = id,
            identifierCode = WOORI_BANK_IDENTIFIER_CODE,
            companyNo = WOORI_BANK_COMPANY_NO,
            institutionCode = WOORI_BANK_INSTITUTION_CODE,
            messageType = messageType,
            transmissionCount = 0,
            messageNo = messageNo,
            transmissionDate = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN),
            transmissionTime = LocalTime.now().convertPatternOf(TIME_BASIC_PATTERN),
            responseCode = responseCode,
            orgMessageNo = orgMessageNo,
            parentAccount = "parentAccount",
            trDate = trDate,
            trTime = trTime,
            tid = "tid",
            trMedium = "01",
            trAmount = trAmount,
            otherCashierCheckAmount = 0,
            etcOtherCashierCheckAmount = 0,
            trBranch = "trBranch",
            depositorName = "depositorName",
            accountNo = accountNo,
            cashDepositYn = "cashDepositYn",
            cashierCheckAmount = 0,
            branchCode = "branchCode",
        )
    }

}