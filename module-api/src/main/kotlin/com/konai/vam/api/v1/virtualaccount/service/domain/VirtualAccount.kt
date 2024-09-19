package com.konai.vam.api.v1.virtualaccount.service.domain

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.CONNECTED
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.DISCONNECTED
import com.konai.vam.core.enumerate.VirtualAccountConnectType
import com.konai.vam.core.enumerate.VirtualAccountStatus
import java.time.LocalDateTime

data class VirtualAccount(
    val id: Long? = null,
    val bankAccount: BankAccount,
    val connectType: VirtualAccountConnectType,
    val status: VirtualAccountStatus,
    var par: String? = null,
    var serviceId: String? = null,
    var cardConnectStatus: VirtualAccountCardConnectStatus = DISCONNECTED,
    var cardConnected: LocalDateTime? = null,
    val cardDisconnected: LocalDateTime? = null,
    var cardSeBatchId: String? = null,
    val parentAccountNo: String? = null,
) {

    fun connectedCard(
        par: String,
        serviceId: String,
        batchId: String,
        cardConnectStatus: VirtualAccountCardConnectStatus,
        cardConnected: LocalDateTime? = LocalDateTime.now()
    ): VirtualAccount {
        this.par = par
        this.serviceId = serviceId
        this.cardConnectStatus = cardConnectStatus
        this.cardConnected = cardConnected
        this.cardSeBatchId = batchId
        return this
    }

    fun checkConnected(): VirtualAccount {
        if (this.cardConnectStatus != CONNECTED && this.par == null && this.serviceId == null) {
            throw InternalServiceException(ErrorCode.VIRTUAL_ACCOUNT_HAS_NOT_CONNECTED_CARD)
        }
        return this
    }

    fun isExistsParAndServiceId(): Boolean {
        return !this.par.isNullOrEmpty() && !this.serviceId.isNullOrEmpty()
    }

}