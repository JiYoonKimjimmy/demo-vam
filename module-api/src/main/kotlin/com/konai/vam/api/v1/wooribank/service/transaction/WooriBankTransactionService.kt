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
import com.konai.vam.core.common.error
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.enumerate.WooriBankResponseCode
import com.konai.vam.core.enumerate.WooriBankResponseCode.`0000`
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WooriBankTransactionService(

    private val wooriBankTransactionMapper: WooriBankTransactionMapper,

    private val rechargeTransactionAdapter: RechargeTransactionAdapter,
    private val rechargeTransactionFindAdapter: RechargeTransactionFindAdapter,
    private val wooriBankAggregationAdapter: WooriBankAggregationAdapter,
    private val virtualAccountFindAdapter: VirtualAccountFindAdapter,
    private val virtualAccountBankFindAdapter: VirtualAccountBankFindAdapter

) : WooriBankTransactionAdapter {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 우리은행 가상계좌 '입금' 처리
     */
    override fun deposit(domain: WooriBankTransaction): WooriBankTransaction {
        return depositProc(domain).let { this.afterProc(it) }
    }

    /**
     * 우리은행 가상계좌 '입금 취소' 처리
     */
    override fun depositCancel(domain: WooriBankTransaction): WooriBankTransaction {
        return depositCancelProc(domain).let { this.afterProc(it) }
    }

    /**
     * 우리은행 가상계좌 '입금 확인 통보' 처리
     */
    override fun depositConfirm(domain: WooriBankTransaction): WooriBankTransaction {
        return depositConfirmProc(domain)
    }

    private fun depositProc(domain: WooriBankTransaction): WooriBankTransaction {
        return try {
            // 가상 계좌 충전 요청 준비
            readyRechargeTransaction(domain)
            // 가상 계좌 충전 요청 처리
            executeRechargeTransaction(domain)
        } catch (e: Exception) {
            // 예외 발생한 경우, 에러 응답 처리
            errorResponse(domain, e)
        }
    }

    private fun depositCancelProc(domain: WooriBankTransaction): WooriBankTransaction {
        return try {
            // 가상 계좌 충전 취소 요청 준비
            readyRechargeCancelTransaction(domain)
            // 가상 계좌 충전 취소 요청 처리
            executeRechargeCancelTransaction(domain)
        } catch (e: Exception) {
            // 예외 발생한 경우, 에러 응답 처리
            errorResponse(domain, e)
        }
    }

    private fun depositConfirmProc(domain: WooriBankTransaction): WooriBankTransaction {
        return try {
            findSuccessRechargeTransaction(domain.tranNo, domain.accountNo)
                .checkIfDepositCanBeConfirmed()
                .let { domain.depositConfirmed().success() }
        } catch (e: Exception) {
            errorResponse(domain, e)
        }
    }

    private fun saveWooriBankAggregationCache(domain: WooriBankTransaction): WooriBankTransaction {
        if (domain.responseCode == `0000`) {
            wooriBankAggregationAdapter.incrementAggregation(
                aggregateDate = domain.trDate,
                tranType = RechargeTransactionType.of(domain.messageType),
                amount = domain.trAmount,
            )
        }
        return domain
    }

    private fun readyRechargeTransaction(domain: WooriBankTransaction): WooriBankTransaction {
        return with(domain) {
            // 가상 계좌 카드 'par' & 'serviceId' 정보 조회
            this.setParAndServiceId(findVirtualAccountCard(domain))
            // 가상 계좌 은행 'rechargerId' 정보 조회
            this.setRechargerId(findVirtualAccountBankRechargerId(domain))
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

    private fun executeRechargeTransaction(domain: WooriBankTransaction): WooriBankTransaction {
        return wooriBankTransactionMapper.domainToRechargeTransaction(domain)
            .let { rechargeTransactionAdapter.recharge(it) }
            .let { checkRechargeTransactionResult(it) }
            .let { domain.success(responseCode = it) }
    }

    private fun executeRechargeCancelTransaction(domain: WooriBankTransaction): WooriBankTransaction {
        return wooriBankTransactionMapper.domainToRechargeCancelTransaction(domain)
            .let { rechargeTransactionAdapter.cancel(it) }
            .let { checkRechargeTransactionResult(it) }
            .let { domain.success(responseCode = it) }
    }

    private fun checkRechargeTransactionResult(rechargeTransaction: RechargeTransaction): WooriBankResponseCode {
        return if (rechargeTransaction.result?.flag == true) {
            `0000`
        } else {
            throw InternalServiceException(rechargeTransaction.errorCode ?: ErrorCode.RECHARGE_TRANSACTION_IS_INVALID)
        }
    }

    private fun findVirtualAccountCard(domain: WooriBankTransaction): VirtualAccount {
        return virtualAccountFindAdapter.findCardConnectedVirtualAccount(domain.accountNo)
            .takeIf { it.isExistsParAndServiceId() }
            // `par` & `serviceId` 가 없는 경우, 예외 발생
            ?: throw ResourceNotFoundException(ErrorCode.VIRTUAL_ACCOUNT_HAS_NOT_CONNECTED_CARD)
    }

    private fun findVirtualAccountBankRechargerId(domain: WooriBankTransaction): String {
        return virtualAccountBankFindAdapter.findByBankCode(domain.bankCode).rechargerId
    }

    private fun findRechargeTransactionId(domain: WooriBankTransaction): String {
        return findSuccessRechargeTransaction(domain.orgTranNo, domain.accountNo)
            .checkIfDepositCanBeCanceled()
            .transactionId!!
    }

    private fun findSuccessRechargeTransaction(tranNo: String, accountNo: String): RechargeTransaction {
        return rechargeTransactionFindAdapter.findSuccessRechargeTransaction(tranNo, accountNo)
    }

    private fun afterProc(domain: WooriBankTransaction): WooriBankTransaction {
        return try {
            // 입금 내역 집계 Cache 업데이트 저장
            saveWooriBankAggregationCache(domain)
        } catch (e: Exception) {
            if (domain.messageType == WooriBankMessageType.VIRTUAL_ACCOUNT_DEPOSIT) {
                // '입금' 요청 예외 발생하는 경우, 충전 내역 완료 거래 취소 처리
                depositCancelProc(domain)
            }
            throw e
        }
    }

    private fun errorResponse(domain: WooriBankTransaction, exception: Exception): WooriBankTransaction {
        logger.error(exception)
        return domain.fail(exception)
    }

}