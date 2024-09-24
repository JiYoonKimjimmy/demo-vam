package com.konai.vam.core.repository.virtualaccount

import com.konai.vam.core.common.annotation.CustomDataJpaTest
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.CONNECTED
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.DISCONNECTED
import com.konai.vam.core.enumerate.VirtualAccountConnectType.FIXATION
import com.konai.vam.core.enumerate.VirtualAccountStatus.ACTIVE
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import fixtures.VirtualAccountEntityFixture
import fixtures.TestExtensionFunctions.generateUUID
import io.kotest.core.spec.style.StringSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import java.security.SecureRandom
import java.util.*

@CustomDataJpaTest
class VirtualAccountRepositoryTest(
    private val virtualAccountJpaRepository: VirtualAccountJpaRepository
) : StringSpec({

    val virtualAccountRepository = VirtualAccountRepository(virtualAccountJpaRepository)
    val virtualAccountEntityFixture = VirtualAccountEntityFixture()
    lateinit var saved: VirtualAccountEntity

    beforeTest {
        val accountNo = generateUUID()
        val entity = virtualAccountEntityFixture.make(accountNo = accountNo)
        saved = virtualAccountRepository.save(entity)
    }

    "가상 계좌 단건 entity 생성하여 저장 성공한다" {
        // given
        val accountNo = generateUUID()
        val entity = virtualAccountEntityFixture.make(accountNo = accountNo)

        // when
        val result = virtualAccountRepository.save(entity)

        // then
        assertThat(result.id).isNotNull()
        assertThat(result.accountNo).isEqualTo(entity.accountNo)
    }

    "가상 계좌 단건 entity 조회 성공한다" {
        // given
        val accountId = saved.id!!

        // when
        val result = virtualAccountRepository.findById(accountId)

        // then
        assertThat(result.id).isEqualTo(accountId)
    }

    "가상 계좌 단건 entity 조회없어 ResourceNotFoundException 예외 발생하여 실패한다" {
        // given
        val id = SecureRandom().nextLong()

        // when
        val exception = assertThrows<ResourceNotFoundException> { virtualAccountRepository.findById(id) }

        // then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.VIRTUAL_ACCOUNT_NOT_FOUND)
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
        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(id)
    }

    "요청 'accountNo' 와 일치한 가상 계좌 다건 조회 성공한다" {
        // given
        val number = 0
        val size = 1
        val pageableRequest = PageableRequest(number, size)
        val predicate = VirtualAccountPredicate(accountNo = saved.accountNo, bankCode = saved.bankCode)

        // when
        val result = virtualAccountRepository.findAllByPredicate(predicate, pageableRequest)

        // then
        assertThat(result.pageable.numberOfElements).isEqualTo(size)
        assertThat(result.content.size).isEqualTo(size)
        assertThat(result.content.first()?.id).isEqualTo(saved.id)
    }

    "요청 'accountNo', 'status', 'cardConnectStatus' 와 일치한 가상 계좌 단건 조회 성공한다" {
        // given
        val accountNo = "1234567890"
        val status = ACTIVE
        val cardConnectStatus = CONNECTED
        val predicate = VirtualAccountPredicate(accountNo = accountNo, status = status, cardConnectStatus = cardConnectStatus)
        virtualAccountEntityFixture.make(accountNo = accountNo, status = status, cardConnectStatus = cardConnectStatus).let(virtualAccountRepository::save)

        // when
        val result = virtualAccountRepository.findByPredicate(predicate)

        // then
        val entity = result.orElse(null)
        assertThat(entity).isNotNull()
        assertThat(entity.accountNo).isEqualTo(accountNo)
        assertThat(entity.status).isEqualTo(status)
        assertThat(entity.cardConnectStatus).isEqualTo(cardConnectStatus)
    }

    "요청 Par 가 이미 가상계좌매핑에 사용되었으면 true 정상 확인한다" {
        // given
        val entity = virtualAccountEntityFixture.make(accountNo =  generateUUID(), par = "par")
        virtualAccountRepository.save(entity)

        // when
        val result = virtualAccountJpaRepository.existsByParIn(listOf("par"))

        // then
        // 결과가 2건이다.
        assertThat(result).isTrue()
    }

    "Par 매핑이 이뤄지지 않은 가상계좌가 한 건 이상이다" {
        // given : 매핑이 이뤄지지 않은 가상 계좌를 생성한다.
        val accountNo =  generateUUID()
        val entity = virtualAccountEntityFixture.make(accountNo =  accountNo, cardConnectStatus = DISCONNECTED, cardSeBatchId = "batchId")
        virtualAccountRepository.save(entity)

        // when : 매핑이 이뤄지지 않은 가상계좌를 조회한다.
        val predicate = VirtualAccountPredicate(
            status = ACTIVE,
            connectType = FIXATION,
            cardConnectStatus = DISCONNECTED,
        )
        val result = virtualAccountRepository.findAllByPredicate(predicate, PageableRequest(number = 0, size = 10))

        // then
        assertThat(result).isNotNull
        assertThat(result.content.size).isGreaterThanOrEqualTo(1)
    }

    "가상 계좌 동일한 'batchId' & 'connectStatus' 존재 여부 확인하여 'true' 정상 확인한다" {
        // given
        val accountNo =  generateUUID()
        val connectStatus = CONNECTED
        val cardSeBatchId = "batchId"
        val entity = virtualAccountEntityFixture.make(accountNo = accountNo, cardConnectStatus = connectStatus, cardSeBatchId = cardSeBatchId)
        virtualAccountRepository.save(entity)

        // when
        val result = virtualAccountRepository.existsByConnectStatusAndBatchId(connectStatus, cardSeBatchId)

        // then
        assertThat(result).isTrue()
    }

    "가상 계좌 동일한 'batchId' & 'connectStatus' 존재 여부 확인하여 'false' 정상 확인한다" {
        // given
        val accountNo =  generateUUID()
        val connectStatus = CONNECTED
        val cardSeBatchId = "batchId"
        virtualAccountRepository.save(virtualAccountEntityFixture.make(accountNo = accountNo, cardConnectStatus = connectStatus, cardSeBatchId = cardSeBatchId))

        // when
        val result = virtualAccountRepository.existsByConnectStatusAndBatchId(connectStatus, "$cardSeBatchId-TEST")

        // then
        assertThat(result).isFalse()
    }

})