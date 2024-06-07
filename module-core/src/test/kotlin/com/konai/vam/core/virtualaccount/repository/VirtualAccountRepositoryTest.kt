package com.konai.vam.core.virtualaccount.repository

import com.konai.vam.core.virtualaccount.repository.entity.VirtualAccountEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

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
    	val result = virtualAccountRepository.getById(accountId)

    	// then
        assertThat(result.id).isEqualTo(accountId)
    }

}