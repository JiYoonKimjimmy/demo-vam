package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.enumerate.VirtualAccountStatus
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import org.springframework.stereotype.Component

@Component
class VirtualAccountModelMapper {

    fun requestToDomain(request: CreateVirtualAccountRequest): VirtualAccount {
        return VirtualAccount(
            accountNo = request.accountNo,
            bankCode = request.bankCode,
            connectType = request.connectType,
            status = VirtualAccountStatus.REGISTERED,
        )
    }

    fun requestToPredicate(request: FindAllVirtualAccountRequest): VirtualAccountPredicate {
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
            accountNo = domain.accountNo,
            bankCode = domain.bankCode,
            status = domain.status,
        )
    }

}