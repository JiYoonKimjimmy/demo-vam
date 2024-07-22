package com.konai.vam.core.repository.rechargetransaction

import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.repository.rechargetransaction.entity.RechargeTransactionEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Repository
class RechargeTransactionRepository(
    private val rechargeTransactionJpaRepository: RechargeTransactionJpaRepository
) : RechargeTransactionEntityAdapter {

    @Transactional
    override fun save(entity: RechargeTransactionEntity): RechargeTransactionEntity {
        return rechargeTransactionJpaRepository.save(entity)
    }

    override fun findByTranNoAndAccountNoAndTranTypeAndResult(tranNo: String, accountNo: String, tranType: RechargeTransactionType, result: Result): RechargeTransactionEntity? {
        return rechargeTransactionJpaRepository.findByTranNoAndAccountNoAndTranTypeAndResult(tranNo, accountNo, tranType, result)
    }
}