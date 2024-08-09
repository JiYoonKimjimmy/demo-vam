package com.konai.vam.core.restclient.wooribank

import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.konai.vam.core.common.model.wooribank.WooriBankCommonModel
import com.konai.vam.core.enumerate.WooriBankMessage

data class PostWooriWorkRequest(
    @JsonUnwrapped
    val model: WooriBankCommonModel
)

data class PostWooriWorkResponse(
    @JsonUnwrapped
    val model: WooriBankCommonModel
)

data class PostWooriAggregateTransactionModel(
    @JsonUnwrapped
    val commonModel: WooriBankCommonModel = WooriBankCommonModel(WooriBankMessage.TRANSACTION_AGGREGATION.requestCode),
    val aggregationDate: String,
    val konaDepositCount: Int,
    val konaDepositAmount: Long,
    val konaDepositCancelCount: Int,
    val konaDepositCancelAmount: Long,
    val konaDepositTrAmount: Long,
    val bankDepositCount: Int = 0,
    val bankDepositAmount: Long = 0,
    val bankDepositCancelCount: Int = 0,
    val bankDepositCancelAmount: Long = 0,
    val bankDepositTrAmount: Long = 0,
)

data class PostWooriAggregateTransactionRequest(
    @JsonUnwrapped
    val model: PostWooriAggregateTransactionModel
)

data class PostWooriAggregateTransactionResponse(
    @JsonUnwrapped
    val model: PostWooriAggregateTransactionModel
)