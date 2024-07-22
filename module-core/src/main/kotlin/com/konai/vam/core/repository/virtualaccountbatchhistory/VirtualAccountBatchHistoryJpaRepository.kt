package com.konai.vam.core.repository.virtualaccountbatchhistory

import com.konai.vam.core.repository.virtualaccountbatchhistory.entity.VirtualAccountBatchHistoryEntity
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface VirtualAccountBatchHistoryJpaRepository : JpaRepository<VirtualAccountBatchHistoryEntity, Long>, KotlinJdslJpqlExecutor {

    fun findByCardSeBatchId(batchId: String): Optional<VirtualAccountBatchHistoryEntity>

}