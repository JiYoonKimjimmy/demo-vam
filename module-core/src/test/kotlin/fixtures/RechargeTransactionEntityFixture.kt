package fixtures

import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.RechargeTransactionType.RECHARGE
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.enumerate.Result.SUCCESS
import com.konai.vam.core.enumerate.RechargeTransactionCancelStatus
import com.konai.vam.core.repository.rechargetransaction.entity.RechargeTransactionEntity
import com.konai.vam.core.repository.virtualaccountbank.VirtualAccountBankConst
import java.security.SecureRandom
import java.time.LocalDateTime

class RechargeTransactionEntityFixture {

    val entities = mutableListOf<RechargeTransactionEntity>()

    fun make(
        id: Long? = SecureRandom().nextLong(),
        tranNo: String = "tranNo0001",
        tranType: RechargeTransactionType = RECHARGE,
        result: Result = SUCCESS,
        reason: String? = null,
        amount: Long = 100000,
        accountNo: String = "1234567890",
        bankCode: String = VirtualAccountBankConst.woori.bankCode,
        par: String = "par",
        serviceId: String = "serviceId",
        transactionId: String = "transactionId0001",
        nrNumber: String = "nrNumber0001",
        rechargerId: String = "rechargerId",
        rechargeDate: LocalDateTime = LocalDateTime.now(),
        rechargeCancelDate: LocalDateTime? = null,
        cancelStatus: RechargeTransactionCancelStatus? = null,
    ): RechargeTransactionEntity {
        return RechargeTransactionEntity(
            id = id,
            tranNo = tranNo,
            tranType = tranType,
            result = result,
            reason = reason,
            amount = amount,
            accountNo = accountNo,
            bankCode = bankCode,
            par = par,
            serviceId = serviceId,
            transactionId = transactionId,
            nrNumber = nrNumber,
            rechargerId = rechargerId,
            rechargeDate = rechargeDate,
            cancelStatus = cancelStatus,
            cancelDate = rechargeCancelDate,
        )
    }

    fun save(entity: RechargeTransactionEntity): RechargeTransactionEntity {
        deleteDuplcated(entity)
        entities += entity
        return entity
    }

    private fun deleteDuplcated(entity: RechargeTransactionEntity) {
        entities.removeIf { (it.id == entity.id) || (it.tranNo == entity.tranNo && it.tranType == entity.tranType && it.result == entity.result ) }
    }

}