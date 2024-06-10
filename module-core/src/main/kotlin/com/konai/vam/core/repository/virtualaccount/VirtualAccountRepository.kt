package com.konai.vam.core.repository.virtualaccount

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import com.konai.vam.core.util.PageRequestUtil.toPageRequest
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class VirtualAccountRepository(
    private val virtualAccountJpaRepository: VirtualAccountJpaRepository
) {

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

    fun findAll(pageableRequest: PageableRequest): BasePageable<VirtualAccountEntity> {
        return BasePageable(virtualAccountJpaRepository.findAll(pageableRequest.toPageRequest()))
    }

}