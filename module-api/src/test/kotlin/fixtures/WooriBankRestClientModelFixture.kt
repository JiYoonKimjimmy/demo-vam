package fixtures

import com.konai.vam.core.common.model.wooribank.WooriBankAggregationMessage
import com.konai.vam.core.enumerate.WooriBankMessageType

class WooriBankRestClientModelFixture {

    fun makePostWooriAggregateTransactionResponse(
        messageNo: String,
        aggregationDate: String,
        konaDepositCount: Int = 0,
        konaDepositAmount: Long = 0,
        konaDepositCancelCount: Int = 0,
        konaDepositCancelAmount: Long = 0,
        konaDepositTrAmount: Long = 0,
        bankDepositCount: Int = 0,
        bankDepositAmount: Long = 0,
        bankDepositCancelCount: Int = 0,
        bankDepositCancelAmount: Long = 0,
        bankDepositTrAmount: Long = 0,
    ): WooriBankAggregationMessage {
        return WooriBankAggregationMessage(
            messageTypeCode = WooriBankMessageType.TRANSACTION_AGGREGATION.requestCode.messageTypeCode,
            businessTypeCode = WooriBankMessageType.TRANSACTION_AGGREGATION.requestCode.businessTypeCode,
            messageNo = messageNo,
            aggregationDate = aggregationDate,
            konaDepositCount = konaDepositCount,
            konaDepositAmount = konaDepositAmount,
            konaDepositCancelCount = konaDepositCancelCount,
            konaDepositCancelAmount = konaDepositCancelAmount,
            konaDepositTrAmount = konaDepositTrAmount,
            bankDepositCount = bankDepositCount,
            bankDepositAmount = bankDepositAmount,
            bankDepositCancelCount = bankDepositCancelCount,
            bankDepositCancelAmount = bankDepositCancelAmount,
            bankDepositTrAmount = bankDepositTrAmount,
        )
    }

}