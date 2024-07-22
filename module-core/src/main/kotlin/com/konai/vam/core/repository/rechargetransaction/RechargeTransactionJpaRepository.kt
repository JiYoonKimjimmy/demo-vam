package com.konai.vam.core.repository.rechargetransaction

import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.repository.rechargetransaction.entity.RechargeTransactionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RechargeTransactionJpaRepository : JpaRepository<RechargeTransactionEntity, Long> {

    fun findByTranNoAndAccountNoAndTranTypeAndResult(tranNo: String, accountNo: String, tranType: RechargeTransactionType, result: Result): RechargeTransactionEntity?

}