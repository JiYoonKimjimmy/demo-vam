package com.konai.vam.core.repository.virtualaccount

import com.konai.vam.core.testsupport.CustomDataJpaTest
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.CONNECTED
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.DISCONNECTED
import com.konai.vam.core.enumerate.VirtualAccountConnectType.FIXATION
import com.konai.vam.core.enumerate.VirtualAccountStatus.ACTIVE
import com.konai.vam.core.repository.parentaccount.ParentAccountJpaRepository
import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import fixtures.ParentAccountEntityFixture
import fixtures.TestExtensionFunctions.generateUUID
import fixtures.VirtualAccountEntityFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.security.SecureRandom
import java.util.*

@CustomDataJpaTest
class VirtualAccountRepositoryTest(
    private val virtualAccountJpaRepository: VirtualAccountJpaRepository,
    private val parentAccountJpaRepository: ParentAccountJpaRepository
) : StringSpec({

    val virtualAccountRepository = VirtualAccountRepository(virtualAccountJpaRepository)
    val virtualAccountEntityFixture = VirtualAccountEntityFixture()
    val parentAccountEntityFixture = ParentAccountEntityFixture()

    lateinit var saveVirtualAccount: VirtualAccountEntity
    lateinit var saveParentAccount: ParentAccountEntity

    val saveVirtualAccountProc: (() -> VirtualAccountEntity) -> Unit = {
        virtualAccountJpaRepository.save(it())
    }

    beforeTest {
        val bankCode = "123"
        val parentAccount = parentAccountEntityFixture.make(parentAccountNo = generateUUID(), bankCode = bankCode)
        saveParentAccount = parentAccountJpaRepository.save(parentAccount)

        val virtualAccount = virtualAccountEntityFixture.make(accountNo = generateUUID(), bankCode = bankCode, parentAccount = saveParentAccount)
        saveVirtualAccount = virtualAccountRepository.save(virtualAccount)
    }

    "가상 계좌 단건 entity 생성하여 저장 성공한다" {
        // given
        val accountNo = generateUUID()
        val entity = virtualAccountEntityFixture.make(accountNo = accountNo, parentAccount = saveParentAccount)

        // when
        val result = virtualAccountRepository.save(entity)

        // then
        result.id shouldNotBe null
        result.accountNo shouldBe entity.accountNo
        result.parentAccount.parentAccountNo shouldBe saveParentAccount.parentAccountNo
    }

    "가상 계좌 단건 entity 조회 성공한다" {
        // given
        val accountId = saveVirtualAccount.id!!

        // when
        val result = virtualAccountRepository.findById(accountId)

        // then
        result.id shouldBe accountId
        result.accountNo shouldBe saveVirtualAccount.accountNo
        result.parentAccount.parentAccountNo shouldBe saveParentAccount.parentAccountNo
    }

    "가상 계좌 단건 entity 조회없어 ResourceNotFoundException 예외 발생하여 실패한다" {
        // given
        val id = SecureRandom().nextLong()

        // when
        val exception = shouldThrow<ResourceNotFoundException> { virtualAccountRepository.findById(id) }

        // then
        exception.errorCode shouldBe ErrorCode.VIRTUAL_ACCOUNT_NOT_FOUND
    }

    "가상 계좌 단건 entity 조회없어 새로운 entity 반환하고 성공한다" {
        // given
        val id = SecureRandom().nextLong()
        val fixture = virtualAccountEntityFixture.make(id)

        // virtualAccountRepository 조회 결과 없는 경우, 신규 entity 생성하여 대체
        val afterProc: ((Optional<VirtualAccountEntity>) -> VirtualAccountEntity) = {
            it.orElse(fixture)
        }

        // when
        val result = virtualAccountRepository.findById(id, afterProc)

        // then
        result shouldNotBe null
        result.id shouldBe id
    }

    "요청 'accountNo' 와 일치한 가상 계좌 다건 조회 성공한다" {
        // given
        val number = 0
        val size = 1
        val pageableRequest = PageableRequest(number, size)
        val predicate = VirtualAccountPredicate(accountNo = saveVirtualAccount.accountNo, bankCode = saveVirtualAccount.bankCode)

        // when
        val result = virtualAccountRepository.findAllByPredicate(predicate, pageableRequest)

        // then
        result.pageable.numberOfElements shouldBe size
        result.content.size shouldBe size
        result.content.first()?.id shouldBe saveVirtualAccount.id
    }

    "요청 'accountNo', 'status', 'cardConnectStatus' 와 일치한 가상 계좌 단건 조회 성공한다" {
        // given
        val accountNo = "1234567890"
        val status = ACTIVE
        val cardConnectStatus = CONNECTED
        val predicate = VirtualAccountPredicate(accountNo = accountNo, status = status, cardConnectStatus = cardConnectStatus)

        saveVirtualAccountProc {
            virtualAccountEntityFixture.make(accountNo = accountNo, status = status, cardConnectStatus = cardConnectStatus, parentAccount = saveParentAccount)
        }

        // when
        val result = virtualAccountRepository.findByPredicate(predicate).orElse(null)

        // then
        result shouldNotBe null
        result.accountNo shouldBe accountNo
        result.status shouldBe status
        result.cardConnectStatus shouldBe cardConnectStatus
    }

    "요청 Par 가 이미 가상계좌매핑에 사용되었으면 true 정상 확인한다" {
        // given
        saveVirtualAccountProc {
            virtualAccountEntityFixture.make(accountNo =  generateUUID(), par = "par", parentAccount = saveParentAccount)
        }

        // when
        val result = virtualAccountJpaRepository.existsByParIn(listOf("par"))

        // then
        result shouldBe true
    }

    "Par 매핑이 이뤄지지 않은 가상계좌가 한 건 이상이다" {
        // given
        val predicate = VirtualAccountPredicate(
            status = ACTIVE,
            connectType = FIXATION,
            cardConnectStatus = DISCONNECTED,
        )

        saveVirtualAccountProc {
            virtualAccountEntityFixture.make(accountNo =  generateUUID(), cardConnectStatus = DISCONNECTED, cardSeBatchId = "batchId", parentAccount = saveParentAccount)
        }

        // when
        val result = virtualAccountRepository.findAllByPredicate(predicate, PageableRequest(number = 0, size = 10))

        // then
        result shouldNotBe null
        result.content shouldHaveAtLeastSize 1
    }

    "가상 계좌 동일한 'batchId' & 'connectStatus' 존재 여부 확인하여 'true' 정상 확인한다" {
        // given
        val accountNo =  generateUUID()
        val connectStatus = CONNECTED
        val cardSeBatchId = "batchId"

        saveVirtualAccountProc {
            virtualAccountEntityFixture.make(accountNo = accountNo, cardConnectStatus = connectStatus, cardSeBatchId = cardSeBatchId, parentAccount = saveParentAccount)
        }

        // when
        val result = virtualAccountRepository.existsByConnectStatusAndBatchId(connectStatus, cardSeBatchId)

        // then
        result shouldBe true
    }

    "가상 계좌 동일한 'batchId' & 'connectStatus' 존재 여부 확인하여 'false' 정상 확인한다" {
        // given
        val accountNo =  generateUUID()
        val connectStatus = CONNECTED
        val cardSeBatchId = "batchId"

        saveVirtualAccountProc {
            virtualAccountEntityFixture.make(accountNo = accountNo, cardConnectStatus = connectStatus, cardSeBatchId = cardSeBatchId, parentAccount = saveParentAccount)
        }
        // when
        val result = virtualAccountRepository.existsByConnectStatusAndBatchId(connectStatus, "$cardSeBatchId-TEST")

        // then
        result shouldBe false
    }

})