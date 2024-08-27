package fixtures

import com.konai.vam.core.common.model.wooribank.WooriBankCommonMessage
import com.konai.vam.core.restclient.wooribank.PostWooriAggregateTransactionModel
import com.konai.vam.core.restclient.wooribank.PostWooriAggregateTransactionResponse

class WooriBankRestClientModelFixture {

    fun makePostWooriAggregateTransactionResponse(
        message: WooriBankCommonMessage,
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
    ): PostWooriAggregateTransactionResponse {
        val model = PostWooriAggregateTransactionModel(
            message = message,
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
        return PostWooriAggregateTransactionResponse(model)
    }

}