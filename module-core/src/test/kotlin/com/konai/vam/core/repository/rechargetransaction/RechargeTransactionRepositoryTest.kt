package com.konai.vam.core.repository.rechargetransaction

import com.konai.vam.core.testsupport.CustomDataJpaTest
import fixtures.RechargeTransactionEntityFixture
import fixtures.TestExtensionFunctions.generateUUID
import io.kotest.core.spec.style.StringSpec
import org.assertj.core.api.Assertions.assertThat

@CustomDataJpaTest
class RechargeTransactionRepositoryTest(
    private val rechargeTransactionJpaRepository: RechargeTransactionJpaRepository
) : StringSpec({

    val rechargeTransactionRepository = RechargeTransactionRepository(rechargeTransactionJpaRepository)
    val rechargeTransactionEntityFixture = RechargeTransactionEntityFixture()

    "CS 시스템 충전 성공하여 충전 내역 정보 저장 성공한다"() {
        // given
        val tranNo = generateUUID()
        val entity = rechargeTransactionEntityFixture.make(tranNo = tranNo)

        // when
        val result = rechargeTransactionRepository.save(entity)

        // then
        assertThat(result).isNotNull
        assertThat(result.id).isNotEqualTo(0)
        assertThat(result.tranNo).isEqualTo(tranNo)
    }

})