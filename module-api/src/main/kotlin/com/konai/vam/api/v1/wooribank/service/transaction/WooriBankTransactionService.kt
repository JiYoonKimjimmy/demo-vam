package com.konai.vam.api.v1.wooribank.service.transaction

import com.konai.vam.api.v1.rechargetransaction.service.RechargeTransactionAdapter
import com.konai.vam.api.v1.rechargetransaction.service.RechargeTransactionFindAdapter
import com.konai.vam.api.v1.rechargetransaction.service.domain.RechargeTransaction
import com.konai.vam.api.v1.virtualaccount.service.VirtualAccountFindAdapter
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccountbank.service.VirtualAccountBankFindAdapter
import com.konai.vam.api.v1.wooribank.service.aggregation.WooriBankAggregationAdapter
import com.konai.vam.api.v1.wooribank.service.transaction.domain.WooriBankTransaction
import com.konai.vam.api.v1.wooribank.service.transaction.domain.WooriBankTransactionMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.RechargeTransactionType.RECHARGE
import com.konai.vam.core.enumerate.WooriBankResponseCode
import com.konai.vam.core.enumerate.WooriBankResponseCode.`0000`
import com.konai.vam.core.util.DATE_BASIC_PATTERN
import com.konai.vam.core.util.convertPatternOf
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class WooriBankTransactionService(

    private val wooriBankTransactionMapper: WooriBankTransactionMapper,

    private val rechargetTransactionAdaptor: RechargeTransactionAdapter,
    private val rechargeTransactionFindAdapter: RechargeTransactionFindAdapter,
    private val wooriBankAggregationAdapter: WooriBankAggregationAdapter,
    private val virtualAccountFindAdapter: VirtualAccountFindAdapter,
    private val virtualAccountBankFindAdapter: VirtualAccountBankFindAdapter

) : WooriBankTransactionAdapter {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun deposit(domain: WooriBankTransaction): WooriBankTransaction {
        return afterRechargeProc(depositProc(domain))
    }

    override fun depositCancel(domain: WooriBankTransaction): WooriBankTransaction {
        return afterRechargeCancelProc(depositCancelProc(domain))
    }

    override fun depositConfirm(domain: WooriBankTransaction): WooriBankTransaction {
        return depositConfirmProc(domain)
    }

    private fun depositProc(domain: WooriBankTransaction): WooriBankTransaction {
        return try {
            // 가상 계좌 충전 요청 준비
            readyRechargeTransaction(domain)
            // 가상 계좌 충전 요청 처리
            requestRechargeTransaction(domain)
        } catch (e: Exception) {
            // 예외 발생한 경우, 에러 응답 처리
            errorResponse(domain, e)
        }
    }

    private fun readyRechargeTransaction(domain: WooriBankTransaction): WooriBankTransaction {
        return with(domain) {
            // 가상 계좌 카드 'par' & 'serviceId' 정보 조회
            this.setParAndServiceId(findVirtualAccountCard(domain))
            // 가상 계좌 은행 'rechargerId' 정보 조회
            this.setRechargerId(findVirtualAccountBankRechargerId(domain))
        }
    }

    private fun findVirtualAccountCard(domain: WooriBankTransaction): VirtualAccount {
        return virtualAccountFindAdapter.findCardConnectedVirtualAccount(domain.accountNo)
            .takeIf { it.par != null && it.serviceId != null }
            ?: throw ResourceNotFoundException(ErrorCode.VIRTUAL_ACCOUNT_HAS_NOT_CONNECTED_CARD)
    }

    private fun findVirtualAccountBankRechargerId(domain: WooriBankTransaction): String {
        return virtualAccountBankFindAdapter.findByBankCode(domain.bankCode).rechargerId
    }

    private fun requestRechargeTransaction(domain: WooriBankTransaction): WooriBankTransaction {
        return wooriBankTransactionMapper.domainToRechargeTransaction(domain)
            .let { rechargetTransactionAdaptor.recharge(it) }
            .let { checkRechargeTransactionResult(it) }
            .let { domain.success(responseCode = it) }
    }

    private fun checkRechargeTransactionResult(rechargeTransaction: RechargeTransaction): WooriBankResponseCode {
        return if (rechargeTransaction.result?.flag == true) {
            `0000`
        } else {
            val errorCode = if (rechargeTransaction.tranType == RECHARGE) ErrorCode.VIRTUAL_ACCOUNT_RECHARGE_FAILED else ErrorCode.VIRTUAL_ACCOUNT_RECHARGE_CANCEL_FAILED
            throw InternalServiceException(errorCode)
        }
    }

    private fun errorResponse(domain: WooriBankTransaction, exception: Exception): WooriBankTransaction {
        logger.error(exception.stackTraceToString())
        return domain.fail(exception)
    }

    private fun afterRechargeProc(domain: WooriBankTransaction): WooriBankTransaction {
        return try {
            // 입금 내역 집계 Cache 업데이트 저장
            saveWooriBankAggregationCache(domain)
        } catch (e: Exception) {
            // 예외 발생하는 경우, 충전 내역 완료 거래 취소 처리
            revertRechargeTranstion(domain, e)
        }
    }

    private fun saveWooriBankAggregationCache(domain: WooriBankTransaction): WooriBankTransaction {
        if (domain.responseCode == `0000`) {
            wooriBankAggregationAdapter.incrementAggregation(
                aggregateDate = LocalDate.now().convertPatternOf(DATE_BASIC_PATTERN),
                tranType = RechargeTransactionType.of(domain.messageTypeCode),
                amount = domain.trAmount,
            )
        }
        return domain
    }

    private fun revertRechargeTranstion(domain: WooriBankTransaction, exception: Exception): Nothing {
        requestRechargeCancelTransaction(domain)
        throw exception
    }

    private fun depositCancelProc(domain: WooriBankTransaction): WooriBankTransaction {
        return try {
            // 가상 계좌 충전 취소 요청 준비
            readyRechargeCancelTransaction(domain)
            // 가상 계좌 충전 취소 요청 처리
            requestRechargeCancelTransaction(domain)
        } catch (e: Exception) {
            // 예외 발생한 경우, 에러 응답 처리
            errorResponse(domain, e)
        }
    }

    private fun readyRechargeCancelTransaction(domain: WooriBankTransaction): WooriBankTransaction {
        return with(domain) {
            // 원거래 충전 내역 'transactionId' 정보 조회
            this.setTransactionId(findRechargeTransactionId(domain))
            // 가상 계좌 카드 'par' & 'serviceId' 정보 조회
            this.setParAndServiceId(findVirtualAccountCard(domain))
        }
    }

    private fun requestRechargeCancelTransaction(domain: WooriBankTransaction): WooriBankTransaction {
        return wooriBankTransactionMapper.domainToRechargeCancleTransaction(domain)
            .let { rechargetTransactionAdaptor.cancel(it) }
            .let { checkRechargeTransactionResult(it) }
            .let { domain.success(responseCode = it) }
    }

    private fun findRechargeTransactionId(domain: WooriBankTransaction): String {
        return findSuccessedRechargeTransaction(domain.orgTranNo, domain.accountNo)
            .checkIfDepositCanBeCanceled()
            .transactionId!!
    }

    private fun depositConfirmProc(domain: WooriBankTransaction): WooriBankTransaction {
        return try {
            findSuccessedRechargeTransaction(domain.tranNo, domain.accountNo)
                .checkIfDepositCanBeConfirmed()
                .takeIf { true }
                .let { domain.confirmed().success(responseCode = `0000`) }
        } catch (e: Exception) {
            errorResponse(domain, e)
        }
    }

    private fun findSuccessedRechargeTransaction(tranNo: String, accountNo: String): RechargeTransaction {
        return rechargeTransactionFindAdapter.findSuccessedRechargeTransaction(tranNo, accountNo)
    }

    private fun afterRechargeCancelProc(domain: WooriBankTransaction): WooriBankTransaction {
        return try {
            // 입금 내역 집계 Cache 업데이트 저장
            saveWooriBankAggregationCache(domain)
        } catch (e: Exception) {
            throw e
        }
    }

}