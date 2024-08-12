package com.konai.vam.api.v1.rechargetransaction.service

import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransaction
import com.konai.vam.core.common.error
import com.konai.vam.core.restclient.cs.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RechargeTransactionService(
    private val rechargeTransactionSaveAdapter: RechargeTransactionSaveAdapter,
    private val rechargeTransactionFindAdapter: RechargeTransactionFindAdapter,
    private val csRestClient: CsRestClient,
) : RechargeTransactionAdapter {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun recharge(domain: RechargeTransaction): RechargeTransaction {
        return saveRechargeTransaction(rechargeProc(domain))
    }

    private fun rechargeProc(domain: RechargeTransaction): RechargeTransaction {
        return try {
            domain.successRecharge(rechargesSystemManualsToCS(domain))
        } catch (e: Exception) {
            errorResponse(domain, e)
        }
    }

    private fun rechargesSystemManualsToCS(domain: RechargeTransaction): CsPostRechargesSystemManualsResponse {
        return CsPostRechargesSystemManualsRequest(
                par = domain.par,
                amount = domain.amount.toString(),
                serviceId = domain.serviceId,
                rechargerId = domain.rechargerId!!,
                isPushRequired = true
            )
            .let { csRestClient.postRechargesSystemManuals(it) }
    }

    override fun cancel(domain: RechargeTransaction): RechargeTransaction {
        return saveRechargeTransaction(cancelProc(domain))
    }

    private fun cancelProc(domain: RechargeTransaction): RechargeTransaction {
        return try {
            domain.successCancel(cancelOriginTransaction(domain))
        } catch (e: Exception) {
            errorResponse(domain, e)
        }
    }

    private fun cancelOriginTransaction(domain: RechargeTransaction): RechargeTransaction {
        val origin = findSuccessRechargeTransaction(domain.cancelOrgTranNo!!, domain.bankAccount.accountNo)
            .also { rechargesSystemManualsReversalToCS(it) }
            .canceled()
        return saveRechargeTransaction(origin)
    }

    private fun findSuccessRechargeTransaction(tranNo: String, accountNo: String): RechargeTransaction {
        return rechargeTransactionFindAdapter.findSuccessRechargeTransaction(tranNo, accountNo)
    }

    private fun rechargesSystemManualsReversalToCS(domain: RechargeTransaction): CsPostRechargesSystemManualsReversalResponse {
        return CsPostRechargesSystemManualsReversalRequest(
                transactionId = domain.transactionId!!,
                par = domain.par,
                amount = domain.amount.toString(),
            )
            .let { csRestClient.postRechargesSystemManualsReversal(it) }
    }

    private fun saveRechargeTransaction(domain: RechargeTransaction): RechargeTransaction {
        return rechargeTransactionSaveAdapter.save(domain)
    }

    private fun errorResponse(domain: RechargeTransaction, exception: Exception): RechargeTransaction {
        logger.error(exception)
        return domain.fail(exception)
    }

}