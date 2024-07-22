package com.konai.vam.core.repository.virtualaccount

import com.konai.vam.core.common.annotation.CustomDataJpaTest
import com.konai.vam.core.enumerate.Result.FAILED
import com.konai.vam.core.enumerate.Result.SUCCESS
import com.konai.vam.core.repository.virtualaccountbatchhistory.VirtualAccountBatchHistoryJpaRepository
import fixtures.VirtualAccountBatchHistoryEntityFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.security.SecureRandom

@CustomDataJpaTest
class VirtualAccountBatchHistoryRepositoryTest {

    @Autowired
    private lateinit var virtualAccountBatchHistoryJpaRepository: VirtualAccountBatchHistoryJpaRepository

    private val virtualAccountBatchHistoryEntityFixture = VirtualAccountBatchHistoryEntityFixture()

    @Test
    fun `배치 정보 단건 저장에 성공한다`(){
        // given : 저장 성공 한 건이 주어진다.
        val oneWithSuccess = virtualAccountBatchHistoryEntityFixture.make(SecureRandom().nextLong(), SUCCESS)

        // when & then: 해당 배치정보를 저장한다.
        virtualAccountBatchHistoryJpaRepository.save(oneWithSuccess)
    }

    @Test
    fun `배치 정보 단건 조회에 성공한다`() {
        // given : 저장 성공 한 건이 주어진다.
        val oneWithSuccess = virtualAccountBatchHistoryEntityFixture.make(SecureRandom().nextLong(), SUCCESS)
        val savedBatchEntity = virtualAccountBatchHistoryJpaRepository.save(oneWithSuccess)
        val savedId = savedBatchEntity.id!!

        // when : 해당 ID 로 조회를 요청한다.
        val foundEntity = virtualAccountBatchHistoryJpaRepository.findById(savedId)

        // then
        assertThat(foundEntity).isNotNull
    }

    @Test
    fun `배치 정보를 성공으로 저장하면 성공이 조회된다`(){

        // given : 배치 정보가 주어진다.
        val oneWithSuccess = virtualAccountBatchHistoryEntityFixture.make(SecureRandom().nextLong(), SUCCESS)
        val savedBatchEntity = virtualAccountBatchHistoryJpaRepository.save(oneWithSuccess)
        val savedId = savedBatchEntity.id!!

        // when : 해당 ID 로 조회를 요청한다.
        val foundEntity = virtualAccountBatchHistoryJpaRepository.findById(savedId)

        // then : 성공으로 조회된다.
        assertThat(foundEntity.get().result).isEqualTo(SUCCESS)

    }

    @Test
    fun `배치 정보를 실패로 저장하면 실패가 조회된다`(){
        // given : 배치 정보가 주어진다.
        val oneWithFailed = virtualAccountBatchHistoryEntityFixture.make(SecureRandom().nextLong(), FAILED)
        val savedBatchEntity = virtualAccountBatchHistoryJpaRepository.save(oneWithFailed)
        val savedId = savedBatchEntity.id!!

        // when : 해당 ID 로 조회를 요청한다.
        val foundEntity = virtualAccountBatchHistoryJpaRepository.findById(savedId)

        // then : 성공으로 조회된다.
        assertThat(foundEntity.get().result).isEqualTo(FAILED)
    }

}