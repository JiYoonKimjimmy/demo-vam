package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import org.springframework.stereotype.Component

@Component
class VirtualAccountModelMapper {

    fun requestToDomain(request: CreateVirtualAccountRequest): VirtualAccount {
        return VirtualAccount(
            accountNumber = request.accountNumber,
            bankCode = request.bankCode,
            bankName = request.bankName,
            status = "CREATED",
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