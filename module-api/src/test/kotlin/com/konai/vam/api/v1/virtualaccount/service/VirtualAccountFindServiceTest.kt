package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.kotestspec.KoTestBehaviorSpec
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.CONNECTED
import com.konai.vam.core.enumerate.VirtualAccountStatus.ACTIVE
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs

class VirtualAccountFindServiceTest : KoTestBehaviorSpec({

    val virtualAccountEntityAdaptor = virtualAccountEntityAdaptor()
    val virtualAccountMapper = virtualAccountMapper()
    val virtualAccountFindService = virtualAccountFindService()

    given("요청 'accountNo', 'status', 'cardConnectStataus' 정보 기준 카드 연결된 가상 계좌 조회 요청하여") {
        val accountNo = "1234567890"
        val status = ACTIVE
        val par = "par$accountNo"
        val cardConnectStatus = CONNECTED
        virtualAccountEntityAdaptor.save(accountNo = accountNo, status = status, par = par, cardConnectStatus = cardConnectStatus)

        `when`("Entity 정보 없는 경우") {
            val exception = shouldThrow<ResourceNotFoundException> {
                virtualAccountFindService.findCardConnectedVirtualAccount("1234")
            }

            then("예외 발생하여 실패한다") {
                exception.errorCode shouldBe ErrorCode.VIRTUAL_ACCOUNT_NOT_FOUND
            }
        }

        `when`("Entity 정보가 있는 경우") {
            val result = virtualAccountFindService.findCardConnectedVirtualAccount(accountNo)
            virtualAccountEntityAdaptor.save(virtualAccountMapper.domainToEntity(result))

            then("도메인 객체 변환하여 반환한다") {
                result shouldNotBeSameInstanceAs VirtualAccount::class
                result.bankAccount.accountNo shouldBe accountNo
                result.par shouldBe par
            }
        }
    }

})