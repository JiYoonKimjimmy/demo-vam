package com.konai.vam.core.repository.rechargetransaction

import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.repository.rechargetransaction.entity.RechargeTransactionEntity

interface RechargeTransactionEntityAdapter {

    fun save(entity: RechargeTransactionEntity): RechargeTransactionEntity

    fun findByTranNoAndAccountNoAndTranTypeAndResult(tranNo: String, accountNo: String, tranType: RechargeTransactionType, result: Result): RechargeTransactionEntity?

}