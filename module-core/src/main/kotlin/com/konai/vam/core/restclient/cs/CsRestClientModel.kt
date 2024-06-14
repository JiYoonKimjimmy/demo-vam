package com.konai.vam.core.restclient.cs

import com.fasterxml.jackson.annotation.JsonProperty
import com.konai.vam.core.enumerate.YesOrNo
import com.konai.vam.core.restclient.cs.CsPostRechargesSystemManualsRequest.SaType.*

data class CsPostRechargesSystemManualsRequest(
    val par: String,
    val amount: String,
    val serviceId: String,
    val rechargerId: String,
    @field:JsonProperty("isPushRequired")
    val isPushRequired: Boolean,

    val saType: SaType = VT_ACN_WOORI,
    val checkWaitingAmtYn: YesOrNo = YesOrNo.Y,
    @field:JsonProperty("isDepositUse")
    val isDepositUse: Boolean = false,
) {
    val url by lazy { "/api/payments/recharges/system/manuals" }

    enum class SaType {
        VT_ACN_WOORI
    }
}

data class CsPostRechargesSystemManualsResponse(
    val userId: String? = null,
    val par: String? = null,
    val amount: String? = null,
    val transactionId: String? = null,
    val nrNumber: String? = null,
    val reason: String? = null,
    val message: String? = null,
)

data class CsPostRechargesSystemManualsReversalRequest(
    val transactionId: String,
    val par: String,
    val amount: Long,
    val saType: CsPostRechargesSystemManualsRequest.SaType = VT_ACN_WOORI,
) {
    val url by lazy { "/api/payments/recharges/system/manuals/reversal" }
}

data class CsPostRechargesSystemManualsReversalResponse(
    val transactionId: String? = null,
    val reason: String? = null,
    val message: String? = null,
)