package com.konai.vam.api.v1.rechargetransaction.service

import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransaction

interface RechargeTransactionFindAdapter {

    fun findSuccessedRechargeTransaction(tranNo: String, accountNo: String): RechargeTransaction

}