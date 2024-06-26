package com.konai.vam.core.repository.virtualaccount

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import com.konai.vam.core.util.PageRequestUtil.toBasePageable
import com.konai.vam.core.util.PageRequestUtil.toPageRequest
import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.JpqlQueryable
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(readOnly = true)
@Repository
class VirtualAccountRepository(
    private val virtualAccountJpaRepository: VirtualAccountJpaRepository
) {

    @Transactional
    fun save(entity: VirtualAccountEntity): VirtualAccountEntity {
        return virtualAccountJpaRepository.save(entity)
    }

    fun findOneById(id: Long, afterProc: ((Optional<VirtualAccountEntity>) -> VirtualAccountEntity)? = null): VirtualAccountEntity {
        val result = virtualAccountJpaRepository.findById(id)
        return if (afterProc != null) {
            afterProc(result)
        } else {
            result.orElseThrow { ResourceNotFoundException(ErrorCode.VIRTUAL_ACCOUNT_NOT_FOUND) }
        }
    }

    fun findPage(predicate: VirtualAccountPredicate, pageableRequest: PageableRequest): BasePageable<VirtualAccountEntity?> {
        val query: (Jpql.() -> JpqlQueryable<SelectQuery<VirtualAccountEntity>>) = {
            select(entity(VirtualAccountEntity::class))
                .from(entity(VirtualAccountEntity::class))
                .whereAnd(
                    predicate.accountNo?.let { path(VirtualAccountEntity::accountNo).eq(it) },
                    predicate.bankCode?.let { path(VirtualAccountEntity::bankCode).eq(it) },
                    predicate.connectType?.let { path(VirtualAccountEntity::connectType).eq(it) }
                )
        }
        return virtualAccountJpaRepository.findPage(pageableRequest.toPageRequest(), query).toBasePageable()
    }

}