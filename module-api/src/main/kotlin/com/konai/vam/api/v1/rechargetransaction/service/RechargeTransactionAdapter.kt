package com.konai.vam.api.v1.rechargetransaction.service

import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransaction

interface RechargeTransactionAdapter {

    fun recharge(domain: RechargeTransaction): RechargeTransaction

    fun cancel(domain: RechargeTransaction): RechargeTransaction

}