package com.konai.vam.core.repository.parentaccount

import com.konai.vam.core.common.getContentFirstOrNull
import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(readOnly = true)
@Repository
class ParentAccountRepository(
    private val parentAccountJpaRepository: ParentAccountJpaRepository
) : ParentAccountEntityAdapter {

    @Transactional
    override fun saveAll(entities: List<ParentAccountEntity>): List<ParentAccountEntity> {
        return parentAccountJpaRepository.saveAll(entities)
    }

    override fun findByPredicate(predicate: ParentAccountPredicate): Optional<ParentAccountEntity> {
        return parentAccountJpaRepository.findSlice(
                pageable = PageRequest.of(0, 1),
                init = predicate.generateQuery()
            )
            .getContentFirstOrNull()
            .let { Optional.ofNullable(it) }
    }

    @Transactional
    override fun delete(id: Long) {
        parentAccountJpaRepository.deleteById(id)
    }

}