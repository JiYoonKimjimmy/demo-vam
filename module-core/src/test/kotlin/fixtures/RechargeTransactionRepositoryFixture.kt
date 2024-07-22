package fixtures

import com.konai.vam.core.enumerate.RechargeTransactionCancelStatus
import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.RechargeTransactionType.RECHARGE
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.repository.rechargetransaction.RechargeTransactionEntityAdapter
import com.konai.vam.core.repository.rechargetransaction.entity.RechargeTransactionEntity

class RechargeTransactionRepositoryFixture : RechargeTransactionEntityAdapter {

    private val rechargeTransactionEntityFixture = RechargeTransactionEntityFixture()

    fun save(
        tranNo: String,
        accountNo: String,
        tranType: RechargeTransactionType = RECHARGE,
        result: Result = Result.SUCCESS,
        cancelStatus: RechargeTransactionCancelStatus? = null
    ): RechargeTransactionEntity {
        val entity = rechargeTransactionEntityFixture.make(tranNo = tranNo, accountNo = accountNo, tranType = tranType, result = result, cancelStatus = cancelStatus)
        return save(entity)
    }

    fun findByTranNoAndTranType(tranNo: String, tranType: RechargeTransactionType = RECHARGE): RechargeTransactionEntity? {
        return rechargeTransactionEntityFixture.entities.find { it.tranNo == tranNo && it.tranType == tranType }
    }

    override fun save(entity: RechargeTransactionEntity): RechargeTransactionEntity {
        return rechargeTransactionEntityFixture.save(entity)
    }

    override fun findByTranNoAndAccountNoAndTranTypeAndResult(tranNo: String, accountNo: String, tranType: RechargeTransactionType, result: Result): RechargeTransactionEntity? {
        return rechargeTransactionEntityFixture.entities
            .find { it.tranNo == tranNo && it.accountNo == accountNo && it.tranType == tranType && it.result == result }
    }

}