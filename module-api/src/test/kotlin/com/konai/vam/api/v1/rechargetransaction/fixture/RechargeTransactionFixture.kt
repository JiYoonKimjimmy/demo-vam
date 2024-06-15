package com.konai.vam.api.v1.rechargetransaction.fixture

import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransaction
import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.Result
import java.time.LocalDateTime

class RechargeTransactionFixture {

    fun getDomain(id: Long? = null, rechargeDate: LocalDateTime = LocalDateTime.now()): RechargeTransaction {
        return RechargeTransaction(
            id = id,
            tranNo = "tranNo0001",
            tranType = RechargeTransactionType.RECHARGE,
            result = Result.SUCCESS,
            reason = null,
            amount = 100000,
            bankCode = "020",
            accountNo = "accountNo",
            par = "par",
            serviceId = "serviceId",
            transactionId = "transactionId0001",
            nrNumber = "nrNumber0001",
            rechargerId = "rechargerId",
            rechargeDate = rechargeDate,
            rechargeCancelDate = null,
            reversalStatus = null,
        )
    }

}