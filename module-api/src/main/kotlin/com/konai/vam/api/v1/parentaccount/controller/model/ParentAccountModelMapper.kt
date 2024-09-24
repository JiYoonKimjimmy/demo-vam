package com.konai.vam.api.v1.parentaccount.controller.model

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import org.springframework.stereotype.Component

@Component
class ParentAccountModelMapper {

    fun requestToDomain(request: CreateParentAccountRequest): ParentAccount {
        return ParentAccount(
            parentAccountNo = request.parentAccountNo,
            bankCode = request.bankCode
        )
    }

    fun domainToModel(domain: ParentAccount): ParentAccountModel {
        return ParentAccountModel(
            parentAccountId = domain.id,
            parentAccountNo = domain.parentAccountNo,
            bankCode = domain.bankCode
        )
    }

    fun requestToPredicate(request: FindAllParentAccountRequest): ParentAccountPredicate {
        return ParentAccountPredicate(
            parentAccountNo = request.parentAccountNo,
            bankCode = request.bankCode
        )
    }

}