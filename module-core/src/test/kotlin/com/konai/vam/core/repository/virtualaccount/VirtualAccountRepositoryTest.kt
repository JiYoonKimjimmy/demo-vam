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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import java.security.SecureRandom
import java.util.*

@CustomDataJpaTest
class VirtualAccountRepositoryTest {

    @Autowired
    private lateinit var virtualAccountJpaRepository: VirtualAccountJpaRepository

    private val virtualAccountRepository by lazy { VirtualAccountRepository(virtualAccountJpaRepository) }
    private val virtualAccountEntityFixture = VirtualAccountEntityFixture()
    private lateinit var saved: VirtualAccountEntity

    @BeforeEach
    fun before() {
        val fixture = virtualAccountEntityFixture.make()
        saved = virtualAccountRepository.save(fixture)
    }

    @Test
    fun `가상 계좌 단건 entity 생성하여 저장 성공한다`() {
    	// given
        val entity = virtualAccountEntityFixture.make()

        // when
        val result = virtualAccountRepository.save(entity)

    	// then
        assertThat(result.id).isNotNull()
        assertThat(result.accountNo).isEqualTo(entity.accountNo)
    }

    @Test
    fun `가상 계좌 단건 entity 조회 성공한다`() {
    	// given
    	val accountId = saved.id!!

    	// when
    	val result = virtualAccountRepository.findById(accountId)

    	// then
        assertThat(result.id).isEqualTo(accountId)
    }

    @Test
    fun `가상 계좌 단건 entity 조회없어 ResourceNotFoundException 예외 발생하여 실패한다`() {
    	// given
    	val accountId = SecureRandom().nextLong()

    	// when
        val exception = assertThrows<ResourceNotFoundException> { virtualAccountRepository.findById(accountId) }

    	// then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.VIRTUAL_ACCOUNT_NOT_FOUND)
    }

    @Test
    fun `가상 계좌 단건 entity 조회없어 새로운 entity 반환하고 성공한다`() {
    	// given
    	val accountId = SecureRandom().nextLong()
        val fixture = virtualAccountEntityFixture.make(accountId)

        // virtualAccountRepository 조회 결과 없는 경우, 신규 entity 생성하여 대체
        val afterProc: ((Optional<VirtualAccountEntity>) -> VirtualAccountEntity) = {
            it.orElse(fixture)
        }

    	// when
        val result = virtualAccountRepository.findById(accountId, afterProc)

    	// then
        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(accountId)
    }

    @Test
    fun `요청 'accountNo' 와 일치한 가상 계좌 다건 조회 성공한다`() {
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

    @Test
    fun `요청 'accountNo', 'status', 'cardConnectStatus' 와 일치한 가상 계좌 단건 조회 성공한다`() {
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

    @Test
    fun `요청 Par 가 이미 가상계좌매핑에 사용되었으면 true 정상 확인한다`(){
        // given
        virtualAccountRepository.save(virtualAccountEntityFixture.make(par = "par"))

        // when
        val result = virtualAccountJpaRepository.existsByParIn(listOf("par"))

        // then
        // 결과가 2건이다.
        assertThat(result).isTrue()
    }

    @Test
    fun `Par 매핑이 이뤄지지 않은 가상계좌가 한 건 이상이다`(){
        // given : 매핑이 이뤄지지 않은 가상 계좌를 생성한다.
        virtualAccountRepository.save(virtualAccountEntityFixture.make(cardConnectStatus = DISCONNECTED, cardSeBatchId = "batchId"))

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

}