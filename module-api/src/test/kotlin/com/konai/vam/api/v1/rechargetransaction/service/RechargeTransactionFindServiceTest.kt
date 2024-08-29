package com.konai.vam.api.v1.rechargetransaction.service

import com.konai.vam.api.v1.kotestspec.CustomBehaviorSpec
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.util.DATE_TIME_BASIC_PATTERN
import com.konai.vam.core.util.convertPatternOf
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class RechargeTransactionFindServiceTest : CustomBehaviorSpec({

    val rechargeTransactionEntityAdaptor = rechargeTransactionEntityAdaptor()
    val rechargeTransactionFindService = rechargeTransactionFindService()

    given("충전 처리 완료 내역 조회 요청하여") {
        val tranNo = LocalDateTime.now().convertPatternOf(DATE_TIME_BASIC_PATTERN)
        val accountNo = "1234567890"
        rechargeTransactionEntityAdaptor.save(tranNo = tranNo, accountNo = accountNo, result = Result.SUCCESS)

        `when`("정보 등록된 경우") {
            val result = rechargeTransactionFindService.findSuccessRechargeTransaction(tranNo, accountNo)

            then("정상 확인한다") {
                result.result shouldBe Result.SUCCESS
            }
        }
        
        `when`("정보 미등록인 경우") {
            val excpetion = shouldThrow<ResourceNotFoundException> { rechargeTransactionFindService.findSuccessRechargeTransaction(tranNo, "${accountNo}unknown") }

            then("ResourceNotFoundException 예외 발생한다") {
                excpetion.errorCode shouldBe ErrorCode.RECHARGE_TRANSACTION_NOT_FOUND
            }
        }
    }

})