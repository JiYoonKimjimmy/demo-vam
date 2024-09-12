package fixtures

import com.konai.vam.core.common.WOORI_BANK_COMPANY_NO
import com.konai.vam.core.common.WOORI_BANK_IDENTIFIER_CODE
import com.konai.vam.core.common.WOORI_BANK_INSTITUTION_CODE
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.enumerate.WooriBankResponseCode
import com.konai.vam.core.enumerate.YesOrNo
import com.konai.vam.core.repository.wooribank.management.entity.WooriBankManagementEntity
import com.konai.vam.core.util.DATE_yyMMdd_PATTERN
import com.konai.vam.core.util.convertPatternOf
import com.konasl.commonlib.springweb.correlation.core.RequestContext
import java.security.SecureRandom
import java.time.LocalDate
import java.time.LocalTime

class WooriBankManagementEntityFixture {

    fun make(
        messageCode: WooriBankMessageType.Code,
        messageNo: String,
        transmissionDate: String = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN),
        responseCode: WooriBankResponseCode? = null,
        accountNo: String = "1234567890",
        trAmount: Long = 100000
    ): WooriBankManagementEntity {
        return WooriBankManagementEntity(
            id = SecureRandom().nextLong(),
            identifierCode = WOORI_BANK_IDENTIFIER_CODE,
            companyNo = WOORI_BANK_COMPANY_NO,
            institutionCode = WOORI_BANK_INSTITUTION_CODE,
            messageTypeCode = messageCode.messageTypeCode,
            businessTypeCode = messageCode.businessTypeCode,
            transmissionCount = 0,
            messageNo = messageNo,
            transmissionDate = transmissionDate,
            transmissionTime = LocalTime.now().convertPatternOf(),
            responseCode = responseCode,
            orgMessageNo = "orgMessageNo",
            parentAccount = "parentAccount",
            trDate = LocalDate.now().convertPatternOf(),
            trTime = LocalTime.now().convertPatternOf(),
            tid = RequestContext.generateId(),
            trMedium = "01",
            trAmount = trAmount,
            otherCashierCheckAmount = 0,
            etcOtherCashierCheckAmount = 0,
            trBranch = "trBranch",
            depositorName = "depositorName",
            accountNo = accountNo,
            accountName = "KONA",
            accountBalance = 0,
            cashDepositYn = "cashDepositYn",
            cashierCheckAmount = 0,
            branchCode = "branchCode",
            depositConfirm = YesOrNo.N,
            responseMessage = null
        )
    }

}