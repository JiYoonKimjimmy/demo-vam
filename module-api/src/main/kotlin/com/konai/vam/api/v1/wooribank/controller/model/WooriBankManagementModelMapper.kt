package com.konai.vam.api.v1.wooribank.controller.model

import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagement
import com.konai.vam.core.enumerate.WooriBankResponseCode
import org.springframework.stereotype.Component

@Component
class WooriBankManagementModelMapper {

    fun requestToDomain(request: WooriBankManagementRequest, tid: String): WooriBankManagement {
        return WooriBankManagement(
            identifierCode = request.identifierCode,
            companyNo = request.companyNo,
            institutionCode = request.institutionCode,
            messageTypeCode = request.messageTypeCode,
            businessTypeCode = request.businessTypeCode,
            transmissionCount = request.transmissionCount,
            messageNo = request.messageNo,
            transmissionDate = request.transmissionDate,
            transmissionTime = request.transmissionTime,
            responseCode = request.responseCode?.takeIf { it.isNotEmpty() }?.let(WooriBankResponseCode::valueOf),
            orgMessageNo = request.orgMessageNo,
            parentAccount = request.parentAccount,
            trDate = request.trDate,
            trTime = request.trTime,
            tid = tid,
            trMedium = request.trMedium,
            trAmount = request.trAmount,
            otherCashierCheckAmount = request.otherCashierCheckAmount,
            etcOtherCashierCheckAmount = request.etcOtherCashierCheckAmount,
            trBranch = request.trBranch,
            depositorName = request.depositorName,
            accountNo = request.accountNo,
            cashDepositYn = request.cashDepositYn,
            cashierCheckAmount = request.cashierCheckAmount,
            branchCode = request.branchCode,
        )
    }

    fun domainToResponse(domain: WooriBankManagement): WooriBankManagementResponse {
        return WooriBankManagementResponse(
            identifierCode = domain.identifierCode,
            companyNo = domain.companyNo,
            institutionCode = domain.institutionCode,
            messageTypeCode = domain.messageTypeCode,
            businessTypeCode = domain.businessTypeCode,
            transmissionCount = domain.transmissionCount,
            messageNo = domain.messageNo,
            transmissionDate = domain.transmissionDate,
            transmissionTime = domain.transmissionTime,
            responseCode = domain.responseCode?.name,
            orgMessageNo = domain.orgMessageNo,
            parentAccount = domain.companyNo,
            trDate = domain.trDate,
            trTime = domain.trTime,
            trMedium = domain.trMedium,
            trAmount = domain.trAmount,
            otherCashierCheckAmount = domain.otherCashierCheckAmount,
            etcOtherCashierCheckAmount = domain.etcOtherCashierCheckAmount,
            trBranch = domain.trBranch,
            depositorName = domain.depositorName,
            accountNo = domain.accountNo,
            accountName = domain.accountName,
            accountBalance = domain.accountBalance.toString(),
        )
    }

}