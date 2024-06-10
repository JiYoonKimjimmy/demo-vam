package com.konai.vam.core.repository.virtualaccount

import com.konai.vam.core.common.EMPTY
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.config.VamCoreTestConfig
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
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

    private lateinit var saved: VirtualAccountEntity

    @BeforeEach
    fun before() {
        saved = VirtualAccountEntity(
            accountNumber = "accountNumber",
            bankCode = "001",
            bankName = "우리은행",
            status = "REGISTERED"
        ).let { virtualAccountRepository.save(it) }
    }

    @Test
    fun `가상 계좌 단건 entity 생성하여 저장 성공한다`() {
    	// given
        val entity = VirtualAccountEntity(accountNumber = "accountNumber", bankCode = "001", bankName = "우리은행", status = "REGISTERED")

        // when
        val result = virtualAccountRepository.save(entity)

    	// then
        assertThat(result.id).isNotNull()
        assertThat(result.accountNumber).isEqualTo(entity.accountNumber)
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
    fun `가상 계좌 단건 entity 조회없어 null 반환하고 성공한다`() {
    	// given
    	val accountId = SecureRandom().nextLong()

    	// when
        val result = virtualAccountRepository.findOneById(accountId) { it.orElse(VirtualAccountEntity(id = accountId, accountNumber = EMPTY, bankCode = EMPTY, bankName = EMPTY, status = EMPTY)) }

    	// then
        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(accountId)
    }

    @Test
    fun `가상 계좌 단건 entity 조회 후 정보 업데이트하여 성공한다`() {
        // given
        val entity = virtualAccountRepository.findOneById(saved.id!!).apply { status = "MAPPING" }

        // when
        val result = virtualAccountRepository.save(entity)

        // then
        assertThat(result.status).isEqualTo("MAPPING")
    }

    @Test
    fun `가상 계좌 다건 조회 성공한다`() {
    	// given
        val number = 1
        val size = 1
    	val pageableRequest = PageableRequest(number, size)

    	// when
    	val result = virtualAccountRepository.findPage(pageableRequest)

    	// then
        assertThat(result.pageable.numberOfElements).isEqualTo(size)
        assertThat(result.content.size).isEqualTo(size)
    }

}