package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.api.v1.virtualaccount.service.domain.BankAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.enumerate.VirtualAccountStatus
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import org.springframework.stereotype.Component

@Component
class VirtualAccountModelMapper {

    fun requestToDomain(request: CreateVirtualAccountRequest): VirtualAccount {
        return VirtualAccount(
            bankAccount = BankAccount(request.accountNo, request.bankCode),
            connectType = request.connectType,
            status = VirtualAccountStatus.ACTIVE,
        )
    }

    fun requestToPredicate(request: FindOneVirtualAccountRequest): VirtualAccountPredicate {
        return VirtualAccountPredicate(
            accountNo = request.accountNo,
            par = request.par
        )
    }

    fun requestToPredicate(request: FindAllVirtualAccountRequest): VirtualAccountPredicate {
        return VirtualAccountPredicate(
            accountNo = request.accountNo,
            bankCode = request.bankCode,
            connectType = request.connectType,
        )
    }

    fun domainToModel(domain: VirtualAccount): VirtualAccountModel {
        return VirtualAccountModel(
            accountNo = domain.bankAccount.accountNo,
            bankCode = domain.bankAccount.bankCode,
            connectType = domain.connectType,
            status = domain.status,
            par = domain.par
        )
    }

}