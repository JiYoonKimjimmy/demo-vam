package com.konai.vam.core.repository.wooribank.fixture

import com.konai.vam.core.repository.wooribank.entity.WooriBankTransactionEntity
import org.junit.jupiter.api.Assertions.*

class WooriBankTransactionFixture {

    fun getEntity(id: Long? = null): WooriBankTransactionEntity {
        return WooriBankTransactionEntity(
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