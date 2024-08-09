package com.konai.vam.api.v1.rechargetransaction.service

import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransaction

interface RechargeTransactionFindAdapter {

    fun findSuccessRechargeTransaction(tranNo: String, accountNo: String): RechargeTransaction

}