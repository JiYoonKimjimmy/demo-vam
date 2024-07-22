package com.konai.vam.api.v1.rechargetransaction.service

import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransaction

interface RechargeTransactionSaveAdapter {

    fun save(domain: RechargeTransaction): RechargeTransaction

}