package com.konai.vam.api.v1.wooribank.service.transaction.domain

import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransaction
import com.konai.vam.api.v1.virtualaccount.service.domain.BankAccount
import com.konai.vam.core.enumerate.RechargeTransactionType.CANCEL
import com.konai.vam.core.enumerate.RechargeTransactionType.RECHARGE
import org.springframework.stereotype.Component

@Component
class WooriBankTransactionMapper {

    fun domainToRechargeTransaction(domain: WooriBankTransaction) : RechargeTransaction {
        return RechargeTransaction(
            tranNo = domain.tranNo,
            tranType = RECHARGE,
            amount = domain.trAmount,
            bankAccount = BankAccount(domain.bankCode, domain.accountNo),
            par = domain.par,
            serviceId = domain.serviceId,
            rechargerId = domain.rechargerId,
        )
    }

    fun domainToRechargeCancelTransaction(domain: WooriBankTransaction) : RechargeTransaction {
        return RechargeTransaction(
            tranNo = domain.tranNo,
            cancelOrgTranNo = domain.orgTranNo,
            tranType = CANCEL,
            amount = domain.trAmount,
            bankAccount = BankAccount(domain.bankCode, domain.accountNo),
            par = domain.par,
            serviceId = domain.serviceId,
            transactionId = domain.transactionId,
        )
    }

}