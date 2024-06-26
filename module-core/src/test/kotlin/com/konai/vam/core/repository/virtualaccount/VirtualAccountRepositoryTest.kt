package com.konai.vam.core.repository.virtualaccount

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.config.VamCoreTestConfig
import com.konai.vam.core.enumerate.VirtualAccountStatus.INACTIVE
import com.konai.vam.core.repository.virtualaccount.fixture.VirtualAccountFixture
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import com.linecorp.kotlinjdsl.support.spring.data.jpa.autoconfigure.KotlinJdslAutoConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import java.security.SecureRandom

@Import(value = [
    VamCoreTestConfig::class,
    KotlinJdslAutoConfiguration::class
])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class VirtualAccountRepositoryTest {

    @Autowired
    private lateinit var virtualAccountJpaRepository: VirtualAccountJpaRepository

    private val virtualAccountRepository by lazy { VirtualAccountRepository(virtualAccountJpaRepository) }
    private val virtualAccountFixture = VirtualAccountFixture()
    private lateinit var saved: VirtualAccountEntity

    @BeforeEach
    fun before() {
        saved = virtualAccountRepository.save(virtualAccountFixture.getEntity())
    }

    @Test
    fun `가상 계좌 단건 entity 생성하여 저장 성공한다`() {
    	// given
        val entity = virtualAccountFixture.getEntity()

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
    	val result = virtualAccountRepository.findOneById(accountId)

    	// then
        assertThat(result.id).isEqualTo(accountId)
    }

    @Test
    fun `가상 계좌 단건 entity 조회없어 ResourceNotFoundException 예외 발생하여 실패한다`() {
    	// given
    	val accountId = SecureRandom().nextLong()

    	// when
        val exception = assertThrows<ResourceNotFoundException> { virtualAccountRepository.findOneById(accountId) }

    	// then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.VIRTUAL_ACCOUNT_NOT_FOUND)
    }

    @Test
    fun `가상 계좌 단건 entity 조회없어 새로운 entity 반환하고 성공한다`() {
    	// given
    	val accountId = SecureRandom().nextLong()

    	// when
        val result = virtualAccountRepository.findOneById(accountId) { it.orElse(virtualAccountFixture.getEntity(accountId)) }

    	// then
        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(accountId)
    }

    @Test
    fun `가상 계좌 단건 entity 조회 후 정보 업데이트하여 성공한다`() {
        // given
        val entity = virtualAccountRepository.findOneById(saved.id!!).apply { this.status = INACTIVE }

        // when
        val result = virtualAccountRepository.save(entity)

        // then
        assertThat(result.status).isEqualTo(INACTIVE)
    }

    @Test
    fun `요청 'accountNo' 와 일치한 가상 계좌 다건 조회 성공한다`() {
    	// given
        val number = 0
        val size = 1
    	val pageableRequest = PageableRequest(number, size)
        val predicate = VirtualAccountPredicate(accountNo = saved.accountNo, bankCode = saved.bankCode)

    	// when
    	val result = virtualAccountRepository.findPage(predicate, pageableRequest)

    	// then
        assertThat(result.pageable.numberOfElements).isEqualTo(size)
        assertThat(result.content.size).isEqualTo(size)
        assertThat(result.content.first()?.id).isEqualTo(saved.id)
    }

}