package com.konai.vam.core.repository.wooribank.management

import com.konai.vam.core.common.getContentFirstOrNull
import com.konai.vam.core.repository.wooribank.management.entity.WooriBankManagementEntity
import com.konai.vam.core.repository.wooribank.management.jdsl.WooriBankManagementPredicate
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(readOnly = true)
@Repository
class WooriBankManagementRepository(
    private val wooriBankManagementJpaRepository: WooriBankManagementJpaRepository
) : WooriBankManagementEntityAdapter {

    @Transactional
    override fun save(entity: WooriBankManagementEntity): WooriBankManagementEntity {
        return wooriBankManagementJpaRepository.save(entity)
    }

    override fun findByPredicate(predicate: WooriBankManagementPredicate): Optional<WooriBankManagementEntity> {
        return wooriBankManagementJpaRepository.findSlice(
                pageable = PageRequest.of(0, 1),
                init = predicate.generateQuery()
            )
            .getContentFirstOrNull()
            .let { Optional.ofNullable(it) }
    }

}