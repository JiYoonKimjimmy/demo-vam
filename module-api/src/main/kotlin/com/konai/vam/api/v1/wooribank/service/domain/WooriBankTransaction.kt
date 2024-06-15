package com.konai.vam.api.v1.wooribank.service.domain

data class WooriBankTransaction(
    val id: Long? = null,
    val messageNo: String,
    val orgMessageNo: String?,
    val messageTypeCode: String,
    val businessTypeCode: String,
    val trDate: String,
    val trTime: String,
    val tid: String,
    val trMedium: String,
    val trAmount: String,
    val selfDrawnBillAmount: String,
    val etcDrawnBillAmount: String,
    val trBranch: String,
    val depositorName: String,
    val accountNo: String,
    val cashReceiptYn: String,
    val selfDrawnCheck: String,
    val depositBranchCode: String,
    val responseCode: String,
    val depositConfirmation: String?,
)