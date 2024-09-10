package com.konai.vam.core.enumerate

enum class RechargeTransactionType {
    RECHARGE,
    CANCEL;

    companion object {
        fun of(messageType: WooriBankMessageType): RechargeTransactionType {
            return when (messageType) {
                WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT -> RECHARGE
                WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT_CANCEL -> CANCEL
                else -> RECHARGE
            }
        }
    }
}