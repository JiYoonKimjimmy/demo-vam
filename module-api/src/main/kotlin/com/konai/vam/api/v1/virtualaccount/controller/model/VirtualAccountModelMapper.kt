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
            status = VirtualAccountStatus.ACTIVE,
        )
    }

    fun requestToPredicate(request: FindAllVirtualAccountRequest): VirtualAccountPredicate {
        return VirtualAccountPredicate(
            accountNo = request.accountNumber,
            bankCode = request.bankCode,
            connectType = request.mappingType,
            isMapping = request.isMapping,
        )
    }

    fun domainToModel(domain: VirtualAccount): VirtualAccountModel {
        return VirtualAccountModel(
            accountNo = domain.accountNo,
            bankCode = domain.bankCode,
            connectType = domain.connectType,
            status = domain.status,
        )
    }

}