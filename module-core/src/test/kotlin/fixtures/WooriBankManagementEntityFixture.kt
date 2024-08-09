package fixtures

import com.konai.vam.core.common.WOORI_BANK_COMPANY_NO
import com.konai.vam.core.common.WOORI_BANK_IDENTIFIER_CODE
import com.konai.vam.core.common.WOORI_BANK_INSTITUTION_CODE
import com.konai.vam.core.enumerate.WooriBankMessage
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

    val entities = mutableListOf<WooriBankManagementEntity>()

    fun make(
        messageCode: WooriBankMessage.WooriBankMessageCode,
        messageNo: String,
        transmissionDate: String = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN),
        responseCode: WooriBankResponseCode? = null,
        accountNo: String = "1234567890",
        trAmount: Long = 100000
    ): WooriBankManagementEntity {
        return findDuplicated(messageCode, messageNo) ?: generateEntity(messageCode, messageNo, transmissionDate, responseCode, accountNo, trAmount)
    }

    fun save(
        messageCode: WooriBankMessage.WooriBankMessageCode,
        messageNo: String,
        transmissionDate: String,
        responseCode: WooriBankResponseCode?
    ): WooriBankManagementEntity {
        return save(make(messageCode, messageNo, transmissionDate, responseCode))
    }

    fun save(entity: WooriBankManagementEntity): WooriBankManagementEntity {
        deleteDuplicated(entity)
        entity.id = SecureRandom().nextLong()
        entities += entity
        return entity
    }

    private fun deleteDuplicated(entity: WooriBankManagementEntity) {
        entities.removeIf {
            it.messageTypeCode == entity.messageTypeCode
            && it.businessTypeCode == entity.businessTypeCode
            && it.messageNo == entity.messageNo
        }
    }

    private fun findDuplicated(messageCode: WooriBankMessage.WooriBankMessageCode, messageNo: String): WooriBankManagementEntity? {
        return entities.find {
            it.messageTypeCode == messageCode.messageTypeCode
            && it.businessTypeCode == messageCode.businessTypeCode
            && it.messageNo == messageNo
        }
    }

    private fun generateEntity(
        messageCode: WooriBankMessage.WooriBankMessageCode,
        messageNo: String,
        transmissionDate: String,
        responseCode: WooriBankResponseCode?,
        accountNo: String,
        trAmount: Long
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