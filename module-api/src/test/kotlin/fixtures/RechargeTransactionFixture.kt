package fixtures

import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransaction
import com.konai.vam.api.v1.virtualaccount.service.domain.BankAccount
import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.RechargeTransactionType.RECHARGE
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.enumerate.RechargeTransactionCancelStatus
import com.konai.vam.core.repository.virtualaccountbank.VirtualAccountBankConst
import java.time.LocalDateTime

class RechargeTransactionFixture {

    fun make(
        id: Long? = null,
        tranNo: String = "tranNo0001",
        tranType: RechargeTransactionType = RECHARGE,
        result: Result? = null,
        reason: String? = null,
        amount: Long = 100000,
        accountNo: String = "1234567890",
        bankCode: String = VirtualAccountBankConst.woori.bankCode,
        par: String = "par",
        serviceId: String = "serviceId",
        transactionId: String? = null,
        nrNumber: String? = null,
        rechargerId: String = "rechargerId",
        rechargeDate: LocalDateTime? = null,
        cancelStatus: RechargeTransactionCancelStatus? = null,
        cancelOrgTranNo: String = tranNo,
        cancelDate: LocalDateTime? = null,
    ): RechargeTransaction {
        return RechargeTransaction(
            id = id,
            tranNo = tranNo,
            tranType = tranType,
            result = result,
            reason = reason,
            amount = amount,
            bankAccount = BankAccount(bankCode, accountNo),
            par = par,
            serviceId = serviceId,
            transactionId = transactionId,
            nrNumber = nrNumber,
            rechargerId = rechargerId,
            rechargeDate = rechargeDate,
            cancelStatus = cancelStatus,
            cancelOrgTranNo = cancelOrgTranNo,
            cancelDate = cancelDate,
        )
    }

}