package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import org.springframework.stereotype.Component

@Component
class VirtualAccountModelMapper {

    fun requestToDomain(request: CreateVirtualAccount.Request): VirtualAccount {
        return VirtualAccount(
            accountNumber = request.accountNumber,
            bankCode = request.bankCode,
            bankName = request.bankName,
            status = "CREATED",
        )
    }

    fun requestToPredicate(request: FindAllVirtualAccount.Request): VirtualAccountPredicate {
        return VirtualAccountPredicate(
            accountNumber = request.accountNumber,
            bankCode = request.bankCode,
            mappingType = request.mappingType,
            isMapping = request.isMapping,
        )
    }

    fun domainToModel(domain: VirtualAccount): VirtualAccountModel {
        return VirtualAccountModel(
            id = domain.id!!,
            accountNumber = domain.accountNumber,
            bankCode = domain.bankCode,
            bankName = domain.bankName,
            status = domain.status,
        )
    }

}