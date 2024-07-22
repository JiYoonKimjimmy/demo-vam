package com.konai.vam.core.repository.virtualaccountbatchhistory

import com.konai.vam.core.common.getContentFirstOrNull
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.virtualaccountbatchhistory.entity.VirtualAccountBatchHistoryEntity
import com.konai.vam.core.repository.virtualaccountbatchhistory.jdsl.VirtualAccountBatchHistoryPredicate
import com.konai.vam.core.util.PageRequestUtil.toPageRequest
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(readOnly = true)
@Repository
class VirtualAccountBatchHistoryRepository(

    private val virtualAccountBatchHistoryJpaRepository: VirtualAccountBatchHistoryJpaRepository,

) : VirtualAccountBatchHistoryEntityAdapter {

    @Transactional
    override fun save(entity: VirtualAccountBatchHistoryEntity): VirtualAccountBatchHistoryEntity {
        return virtualAccountBatchHistoryJpaRepository.save(entity)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun saveAndFlush(entity: VirtualAccountBatchHistoryEntity): VirtualAccountBatchHistoryEntity {
        return virtualAccountBatchHistoryJpaRepository.saveAndFlush(entity)
    }

    override fun findById(id: Long): Optional<VirtualAccountBatchHistoryEntity> {
        return virtualAccountBatchHistoryJpaRepository.findById(id)
    }

    override fun findByCardSeBatchId(batchId: String): Optional<VirtualAccountBatchHistoryEntity> {
        return virtualAccountBatchHistoryJpaRepository.findByCardSeBatchId(batchId)
    }

    override fun findByPredicate(predicate: VirtualAccountBatchHistoryPredicate, pageableRequest: PageableRequest): Optional<VirtualAccountBatchHistoryEntity> {
        return virtualAccountBatchHistoryJpaRepository.findPage(
                pageable = pageableRequest.toPageRequest(),
                init = predicate.generateQuery()
            )
            .getContentFirstOrNull()
            .let { Optional.ofNullable(it) }
    }

}