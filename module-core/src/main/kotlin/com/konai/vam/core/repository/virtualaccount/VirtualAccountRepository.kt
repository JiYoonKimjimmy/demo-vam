package com.konai.vam.core.repository.virtualaccount

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.getContentFirstOrNull
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import com.konai.vam.core.util.PageRequestUtil.toBasePageable
import com.konai.vam.core.util.PageRequestUtil.toPageRequest
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(readOnly = true)
@Repository
class VirtualAccountRepository(
    private val virtualAccountJpaRepository: VirtualAccountJpaRepository
) : VirtualAccountEntityAdapter {

    @Transactional
    override fun save(entity: VirtualAccountEntity): VirtualAccountEntity {
        return virtualAccountJpaRepository.save(entity)
    }

    override fun saveAll(entities: List<VirtualAccountEntity>): List<VirtualAccountEntity> {
        return virtualAccountJpaRepository.saveAll(entities)
    }

    override fun findById(id: Long, afterProc: ((Optional<VirtualAccountEntity>) -> VirtualAccountEntity)?): VirtualAccountEntity {
        val result = virtualAccountJpaRepository.findById(id)
        return if (afterProc != null) {
            afterProc(result)
        } else {
            result.orElseThrow { ResourceNotFoundException(ErrorCode.VIRTUAL_ACCOUNT_NOT_FOUND) }
        }
    }

    override fun findByPredicate(predicate: VirtualAccountPredicate): Optional<VirtualAccountEntity> {
        return virtualAccountJpaRepository.findSlice(
                pageable = PageRequest.of(0, 1),
                init = predicate.generateQuery()
            )
            .getContentFirstOrNull()
            .let { Optional.ofNullable(it) }
    }

    override fun findAllByPredicate(predicate: VirtualAccountPredicate, pageableRequest: PageableRequest): BasePageable<VirtualAccountEntity?> {
        return virtualAccountJpaRepository.findPage(
            pageable = pageableRequest.toPageRequest(),
            init = predicate.generateQuery()
        ).toBasePageable()
    }

    override fun existsByPars(pars: List<String>): Boolean {
        return virtualAccountJpaRepository.existsByParIn(pars)
    }
}