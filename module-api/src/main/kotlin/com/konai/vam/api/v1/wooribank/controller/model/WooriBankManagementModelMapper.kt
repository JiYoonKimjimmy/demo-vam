package com.konai.vam.api.v1.wooribank.controller.model

import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagement
import org.springframework.stereotype.Component

@Component
class WooriBankManagementModelMapper {

    fun requestToDomain(request: WooriBankManagementRequest, tid: String): WooriBankManagement {
        return WooriBankManagement(
            identifierCode = request.management.common.identifierCode,
            companyNo = request.management.common.companyNo,
            institutionCode = request.management.common.institutionCode,
            messageTypeCode = request.management.common.messageTypeCode,
            businessTypeCode = request.management.common.businessTypeCode,
            transmissionCount = request.management.common.transmissionCount,
            messageNo = request.management.common.messageNo,
            transmissionDate = request.management.common.transmissionDate,
            transmissionTime = request.management.common.transmissionTime,
            responseCode = request.management.common.responseCode,
            orgMessageNo = request.management.common.orgMessageNo,
            parentAccount = request.management.parentAccount,
            trDate = request.management.trDate,
            trTime = request.management.trTime,
            tid = tid,
            trMedium = request.management.trMedium,
            trAmount = request.management.trAmount,
            otherCashierCheckAmount = request.management.selfDrawnBillAmount,
            etcOtherCashierCheckAmount = request.management.etcDrawnBillAmount,
            trBranch = request.management.trBranch,
            depositorName = request.management.depositorName,
            accountNo = request.management.accountNo,
            cashDepositYn = request.cashDepositYn,
            cashierCheckAmount = request.selfDrawnCheckAmount,
            branchCode = request.branchCode,
        )
    }

    fun domainToResponse(domain: WooriBankManagement): WooriBankManagementResponse {
        return WooriBankManagementResponse(
            management = WooriBankManagementModel(
                common = WooriBankCommonModel(
                    identifierCode = domain.identifierCode,
                    companyNo = domain.companyNo,
                    institutionCode = domain.institutionCode,
                    messageTypeCode = domain.messageTypeCode,
                    businessTypeCode = domain.businessTypeCode,
                    transmissionCount = domain.transmissionCount,
                    messageNo = domain.messageNo,
                    transmissionDate = domain.transmissionDate,
                    transmissionTime = domain.transmissionTime,
                    responseCode = domain.responseCode,
                    orgMessageNo = domain.orgMessageNo,
                ),
                parentAccount = domain.companyNo,
                trDate = domain.trDate,
                trTime = domain.trTime,
                trMedium = domain.trMedium,
                trAmount = domain.trAmount,
                selfDrawnBillAmount = domain.otherCashierCheckAmount,
                etcDrawnBillAmount = domain.etcOtherCashierCheckAmount,
                trBranch = domain.trBranch,
                depositorName = domain.depositorName,
                accountNo = domain.accountNo,
            ),
            accountName = domain.accountName,
            accountBalance = domain.accountBalance.toString(),
        )
    }

}