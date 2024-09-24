package com.konai.vam.core.repository.parentaccount

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.getContentFirstOrNull
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import com.konai.vam.core.util.PageRequestUtil.toBasePageable
import com.konai.vam.core.util.PageRequestUtil.toPageRequest
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
    override fun save(entity: ParentAccountEntity): ParentAccountEntity {
        return parentAccountJpaRepository.save(entity)
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

    override fun checkDuplicated(entity: ParentAccountEntity): ParentAccountEntity {
        return if (parentAccountJpaRepository.existsByParentAccountNoAndBankCode(entity.parentAccountNo, entity.bankCode)) {
            throw InternalServiceException(ErrorCode.PARENT_ACCOUNT_NO_IS_DUPLICATED)
        } else {
            entity
        }
    }

    override fun findAllByPredicate(predicate: ParentAccountPredicate, pageableRequest: PageableRequest): BasePageable<ParentAccountEntity?> {
        return parentAccountJpaRepository.findSlice(
            pageable = pageableRequest.toPageRequest(),
            init = predicate.generateQuery()
        ).toBasePageable()
    }

}