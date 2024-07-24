package com.konai.vam.api.v1.wooribank.service.management.domain

import com.konai.vam.api.v1.wooribank.service.transaction.domain.WooriBankTransaction
import com.konai.vam.core.repository.wooribank.management.entity.WooriBankManagementEntity
import com.konai.vam.core.repository.wooribank.management.jdsl.WooriBankManagementPredicate
import org.springframework.stereotype.Component

@Component
class WooriBankManagementMapper {

    fun domainToPredicate(domain: WooriBankManagement): WooriBankManagementPredicate {
        return WooriBankManagementPredicate(
            messageTypeCode = domain.messageTypeCode,
            businessTypeCode = domain.businessTypeCode,
            messageNo = domain.messageNo,
            transmissionDate = domain.transmissionDate
        )
    }

    fun entityToDomain(entity: WooriBankManagementEntity): WooriBankManagement {
        return WooriBankManagement(
            identifierCode = entity.identifierCode,
            companyNo = entity.companyNo,
            institutionCode = entity.institutionCode,
            messageTypeCode = entity.messageTypeCode,
            businessTypeCode = entity.businessTypeCode,
            transmissionCount = entity.transmissionCount,
            messageNo = entity.messageNo,
            transmissionDate = entity.transmissionDate,
            transmissionTime = entity.transmissionTime,
            responseCode = entity.responseCode,
            orgMessageNo = entity.orgMessageNo,
            parentAccount = entity.parentAccount,
            trDate = entity.trDate,
            trTime = entity.trTime,
            tid = entity.tid,
            trMedium = entity.trMedium,
            trAmount = entity.trAmount.toInt(),
            otherCashierCheckAmount = entity.otherCashierCheckAmount.toInt(),
            etcOtherCashierCheckAmount = entity.etcOtherCashierCheckAmount.toInt(),
            trBranch = entity.trBranch,
            depositorName = entity.depositorName,
            accountNo = entity.accountNo,
            accountName = entity.accountName,
            accountBalance = entity.accountBalance,
            cashDepositYn = entity.cashDepositYn,
            cashierCheckAmount = entity.cashierCheckAmount.toInt(),
            branchCode = entity.branchCode,
            depositConfirm = entity.depositConfirm,
            responseMessage = entity.responseMessage
        )
    }

    fun domainToEntity(domain: WooriBankManagement): WooriBankManagementEntity {
        return WooriBankManagementEntity(
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
            parentAccount = domain.parentAccount,
            trDate = domain.trDate,
            trTime = domain.trTime,
            tid = domain.tid,
            trMedium = domain.trMedium,
            trAmount = domain.trAmount.toLong(),
            otherCashierCheckAmount = domain.otherCashierCheckAmount.toLong(),
            etcOtherCashierCheckAmount = domain.etcOtherCashierCheckAmount.toLong(),
            trBranch = domain.trBranch,
            depositorName = domain.depositorName,
            accountNo = domain.accountNo,
            accountName = domain.accountName,
            accountBalance = domain.accountBalance,
            cashDepositYn = domain.cashDepositYn,
            cashierCheckAmount = domain.cashierCheckAmount.toLong(),
            branchCode = domain.branchCode,
            depositConfirm = domain.depositConfirm,
            responseMessage= domain.responseMessage
        )
    }

    fun domainToTransaction(domain: WooriBankManagement): WooriBankTransaction {
        return WooriBankTransaction(
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
            parentAccount = domain.parentAccount,
            trDate = domain.trDate,
            trTime = domain.trTime,
            tid = domain.tid,
            trMedium = domain.trMedium,
            trAmount = domain.trAmount.toLong(),
            otherCashierCheckAmount = domain.otherCashierCheckAmount.toLong(),
            etcOtherCashierCheckAmount = domain.etcOtherCashierCheckAmount.toLong(),
            trBranch = domain.trBranch,
            depositorName = domain.depositorName,
            accountNo = domain.accountNo,
            cashDepositYn = domain.cashDepositYn,
            cashierCheckAmount = domain.cashierCheckAmount.toLong(),
            branchCode = domain.branchCode,
        )
    }

    fun transactionToDomain(transaction: WooriBankTransaction): WooriBankManagement {
        return WooriBankManagement(
            identifierCode = transaction.identifierCode,
            companyNo = transaction.companyNo,
            institutionCode = transaction.institutionCode,
            messageTypeCode = transaction.messageTypeCode,
            businessTypeCode = transaction.businessTypeCode,
            transmissionCount = transaction.transmissionCount,
            messageNo = transaction.messageNo,
            transmissionDate = transaction.transmissionDate,
            transmissionTime = transaction.transmissionTime,
            responseCode = transaction.responseCode,
            orgMessageNo = transaction.orgMessageNo,
            parentAccount = transaction.parentAccount,
            trDate = transaction.trDate,
            trTime = transaction.trTime,
            tid = transaction.tid,
            trMedium = transaction.trMedium,
            trAmount = transaction.trAmount.toInt(),
            otherCashierCheckAmount = transaction.otherCashierCheckAmount.toInt(),
            etcOtherCashierCheckAmount = transaction.etcOtherCashierCheckAmount.toInt(),
            trBranch = transaction.trBranch,
            depositorName = transaction.depositorName,
            accountNo = transaction.accountNo,
            cashDepositYn = transaction.cashDepositYn,
            cashierCheckAmount = transaction.cashierCheckAmount.toInt(),
            branchCode = transaction.branchCode,
            depositConfirm = transaction.depositConfirm,
            responseMessage = transaction.responseMessage,
        )
    }

}