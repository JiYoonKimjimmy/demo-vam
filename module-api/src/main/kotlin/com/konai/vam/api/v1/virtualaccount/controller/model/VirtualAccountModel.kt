package com.konai.vam.api.v1.virtualaccount.controller.model

import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus
import com.konai.vam.core.enumerate.VirtualAccountConnectType
import com.konai.vam.core.enumerate.VirtualAccountStatus
import java.time.LocalDateTime

data class VirtualAccountModel(
    val accountNo: String,
    val bankCode: String,
    val connectType: VirtualAccountConnectType,
    val status: VirtualAccountStatus,
    val par: String? = null,
    val cardConnectStatus: VirtualAccountCardConnectStatus? = null,
    val cardConnected: LocalDateTime? = null,
    val cardDisconnected: LocalDateTime? = null,
    val cardSeBatchId: String? = null,
)