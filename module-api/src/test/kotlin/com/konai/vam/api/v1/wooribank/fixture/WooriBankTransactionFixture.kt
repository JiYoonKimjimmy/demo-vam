package com.konai.vam.api.v1.wooribank.fixture

import com.konai.vam.api.v1.wooribank.service.domain.WooriBankTransaction
import org.junit.jupiter.api.Assertions.*

class WooriBankTransactionFixture {

    fun getDomain(id: Long? = null): WooriBankTransaction {
        return WooriBankTransaction(
            id = id,
            messageNo = "messageNo",
            orgMessageNo = "orgMessageNo",
            messageTypeCode = "messageTypeCode",
            businessTypeCode = "businessTypeCode",
            trDate = "trDate",
            trTime = "trTime",
            tid = "tid",
            trMedium = "trMedium",
            trAmount = "trAmount",
            selfDrawnBillAmount = "selfDrawnBillAmount",
            etcDrawnBillAmount = "etcDrawnBillAmount",
            trBranch = "trBranch",
            depositorName = "depositorName",
            accountNo = "accountNo",
            cashReceiptYn = "cashReceiptYn",
            selfDrawnCheck = "selfDrawnCheck",
            depositBranchCode = "depositBranchCode",
            responseCode = "responseCode",
            depositConfirmation = "depositConfirmation",
        )
    }

}