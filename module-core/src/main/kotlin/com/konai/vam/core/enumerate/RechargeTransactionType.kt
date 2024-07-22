package com.konai.vam.core.enumerate

enum class RechargeTransactionType {
    RECHARGE,
    CANCEL;

    companion object {
        fun of(messageTypeCode: String): RechargeTransactionType {
            return when (messageTypeCode) {
                "0200" -> RECHARGE
                "0420" -> CANCEL
                else -> RECHARGE
            }
        }
    }
}