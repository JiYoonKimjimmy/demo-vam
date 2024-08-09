package com.konai.vam.api.v1.rechargetransaction.service

import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransaction
import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransactionMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.enumerate.RechargeTransactionType.RECHARGE
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.repository.rechargetransaction.RechargeTransactionEntityAdapter
import org.springframework.stereotype.Service

@Service
class RechargeTransactionFindService(
    private val rechargeTransactionEntityAdapter: RechargeTransactionEntityAdapter,
    private val rechargeTransactionMapper: RechargeTransactionMapper,
) : RechargeTransactionFindAdapter {

    override fun findSuccessRechargeTransaction(tranNo: String, accountNo: String): RechargeTransaction {
        return rechargeTransactionEntityAdapter.findByTranNoAndAccountNoAndTranTypeAndResult(tranNo, accountNo, RECHARGE, Result.SUCCESS)
            .takeIf { it != null }
            ?.let { rechargeTransactionMapper.entityToDomain(it) }
            ?: throw ResourceNotFoundException(ErrorCode.RECHARGE_TRANSACTION_NOT_FOUND)
    }

}