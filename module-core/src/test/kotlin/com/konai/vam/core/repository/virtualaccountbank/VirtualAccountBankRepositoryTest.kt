package com.konai.vam.core.repository.virtualaccountbank

import com.konai.vam.core.common.annotation.CustomDataJpaTest
import com.konai.vam.core.repository.virtualaccountbank.jdsl.VirtualAccountBankPredicate
import fixtures.VirtualAccountBankEntityFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@CustomDataJpaTest
class VirtualAccountBankRepositoryTest {

    @Autowired
    private lateinit var virtualAccountBankJpaRepository: VirtualAccountBankJpaRepository

    private val virtualAccountBankRepository by lazy { VirtualAccountBankRepository(virtualAccountBankJpaRepository) }
    private val virtualAccountBankEntityFixture = VirtualAccountBankEntityFixture()

    @BeforeEach
    fun before() {
        val entity = virtualAccountBankEntityFixture.make(VirtualAccountBankConst.woori)
        virtualAccountBankRepository.save(entity)
    }

    @Test
    fun `'020' 은행 코드 기준 가상 계좌 은행 정보 조회 성공한다`() {
    	// given
        val bankCode = VirtualAccountBankConst.woori.bankCode

    	// when
        val result = virtualAccountBankRepository.findByBankCode(bankCode)

    	// then
        assertThat(result).isNotNull
        assertThat(result.bankCode).isEqualTo(bankCode)
    }

    @Test
    fun `'020' 은행 코드 요청 기준 가상 계좌 은행 정보 전체 1건 조회 성공한다`() {
    	// given
    	val predicate = VirtualAccountBankPredicate(bankCode = VirtualAccountBankConst.woori.bankCode)

    	// when
        val result = virtualAccountBankRepository.findAllByPredicate(predicate)

        // then
        assertThat(result).isNotNull
        assertThat(result.content).hasSizeGreaterThanOrEqualTo(1)
    }

    @Test
    fun `'020' 은행 코드 '우리 윤행' 요청 기준 가상 계좌 은행 정보 전체 0건 조회 성공한다`() {
    	// given
    	val predicate = VirtualAccountBankPredicate(bankCode = VirtualAccountBankConst.woori.bankCode, bankName = "우리 윤행")

    	// when
        val result = virtualAccountBankRepository.findAllByPredicate(predicate)

        // then
        assertThat(result).isNotNull
        assertThat(result.content).isEmpty()
    }

}