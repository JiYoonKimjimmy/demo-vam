package com.konai.vam.core.repository.virtualaccountbatchhistory

import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.virtualaccountbatchhistory.entity.VirtualAccountBatchHistoryEntity
import com.konai.vam.core.repository.virtualaccountbatchhistory.jdsl.VirtualAccountBatchHistoryPredicate
import java.util.*

interface VirtualAccountBatchHistoryEntityAdapter {

    fun save(entity: VirtualAccountBatchHistoryEntity): VirtualAccountBatchHistoryEntity

    fun saveAndFlush(entity: VirtualAccountBatchHistoryEntity): VirtualAccountBatchHistoryEntity

    fun findById(id: Long): Optional<VirtualAccountBatchHistoryEntity>

    fun findByCardSeBatchId(batchId: String): Optional<VirtualAccountBatchHistoryEntity>

    fun findByPredicate(predicate: VirtualAccountBatchHistoryPredicate, pageableRequest: PageableRequest): Optional<VirtualAccountBatchHistoryEntity>
}