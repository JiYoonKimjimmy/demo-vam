package com.konai.vam.api.v1.virtualaccountbank.controller.model

import com.konai.vam.api.v1.virtualaccountbank.service.domain.VirtualAccountBank
import com.konai.vam.core.repository.virtualaccountbank.jdsl.VirtualAccountBankPredicate
import org.springframework.stereotype.Component

@Component
class VirtualAccountBankModelMapper {

    fun domainToModel(domain: VirtualAccountBank): VirtualAccountBankModel {
        return VirtualAccountBankModel(
            bankCode = domain.bankCode,
            bankName = domain.bankName,
            bankEngName = domain.bankEngName,
            rechargerId = domain.rechargerId
        )
    }

    fun requestToPredicate(request: FindAllVirtualAccountBankRequest): VirtualAccountBankPredicate {
        return VirtualAccountBankPredicate(
            bankCode = request.bankCode,
            bankName = request.bankName
        )
    }

}