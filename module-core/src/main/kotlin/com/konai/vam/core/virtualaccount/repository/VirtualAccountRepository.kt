package com.konai.vam.core.virtualaccount.repository

import com.konai.vam.core.virtualaccount.repository.entity.VirtualAccountEntity
import org.springframework.stereotype.Repository

@Repository
class VirtualAccountRepository(
    private val virtualAccountJpaRepository: VirtualAccountJpaRepository
) {

    fun save(entity: VirtualAccountEntity): VirtualAccountEntity {
        return virtualAccountJpaRepository.save(entity)
    }

    fun getById(id: Long): VirtualAccountEntity {
        return virtualAccountJpaRepository.findById(id).orElseThrow()
    }

}