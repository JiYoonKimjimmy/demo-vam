package com.konai.vam.core.repository.rechargetransaction

import com.konai.vam.core.common.annotation.CustomDataJpaTest
import fixtures.RechargeTransactionEntityFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@CustomDataJpaTest
class RechargeTransactionRepositoryTest {

    @Autowired
    private lateinit var rechargeTransactionJpaRepository: RechargeTransactionJpaRepository

    private val rechargeTransactionRepository by lazy { RechargeTransactionRepository(rechargeTransactionJpaRepository) }

    private val rechargeTransactionEntityFixture = RechargeTransactionEntityFixture()

    @Test
    fun `CS 시스템 충전 성공하여 충전 내역 정보 저장 성공한다`() {
    	// given
        val tranNo = UUID.randomUUID().toString()
        val entity = rechargeTransactionEntityFixture.make(tranNo = tranNo)

    	// when
        val result = rechargeTransactionRepository.save(entity)

    	// then
        assertThat(result).isNotNull
        assertThat(result.id).isNotEqualTo(0)
        assertThat(result.tranNo).isEqualTo(tranNo)
    }


}