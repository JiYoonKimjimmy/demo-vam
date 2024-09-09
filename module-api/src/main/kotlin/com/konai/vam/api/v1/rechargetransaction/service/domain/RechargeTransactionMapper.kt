package com.konai.vam.api.v1.rechargetransaction.service.domain

import com.konai.vam.api.v1.virtualaccount.service.domain.BankAccount
import com.konai.vam.core.enumerate.Result.FAILED
import com.konai.vam.core.repository.rechargetransaction.entity.RechargeTransactionEntity
import com.konai.vam.core.restclient.cs.CsPostRechargesSystemManualsRequest
import org.springframework.stereotype.Component

@Component
class RechargeTransactionMapper {

    fun domainToEntity(domain: RechargeTransaction): RechargeTransactionEntity {
        return RechargeTransactionEntity(
            id = domain.id,
            tranNo = domain.tranNo,
            tranType = domain.tranType,
            result = domain.result ?: FAILED,
            amount = domain.amount,
            accountNo = domain.bankAccount.accountNo,
            bankCode = domain.bankAccount.bankCode,
            par = domain.par,
            serviceId = domain.serviceId,
            transactionId = domain.transactionId,
            nrNumber = domain.nrNumber,
            rechargerId = domain.rechargerId,
            rechargeDate = domain.rechargeDate,
            cancelStatus = domain.cancelStatus,
            cancelOrgTranNo = domain.cancelOrgTranNo,
            cancelDate = domain.cancelDate,
            reason = domain.reason,
        )
    }

    fun entityToDomain(entity: RechargeTransactionEntity): RechargeTransaction {
        return RechargeTransaction(
            id = entity.id,
            tranNo = entity.tranNo,
            tranType = entity.tranType,
            result = entity.result,
            amount = entity.amount,
            bankAccount = BankAccount(entity.accountNo, entity.bankCode),
            par = entity.par,
            serviceId = entity.serviceId,
            transactionId = entity.transactionId,
            nrNumber = entity.nrNumber,
            rechargerId = entity.rechargerId,
            rechargeDate = entity.rechargeDate,
            cancelStatus = entity.cancelStatus,
            cancelOrgTranNo = entity.cancelOrgTranNo,
            cancelDate = entity.cancelDate,
            reason = entity.reason,
        )
    }

    fun domainToCsPostRechargesSystemManualsRequest(domain: RechargeTransaction): CsPostRechargesSystemManualsRequest {
        return CsPostRechargesSystemManualsRequest(
            par = domain.par,
            amount = domain.amount.toString(),
            serviceId = domain.serviceId,
            rechargerId = domain.rechargerId!!,
            isPushRequired = true,
            isOverRcgAmtLimit = true
        )
    }

}