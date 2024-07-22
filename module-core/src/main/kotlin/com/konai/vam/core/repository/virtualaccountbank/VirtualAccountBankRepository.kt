package com.konai.vam.core.repository.virtualaccountbank

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.repository.virtualaccountbank.entity.VirtualAccountBankEntity
import com.konai.vam.core.repository.virtualaccountbank.jdsl.VirtualAccountBankPredicate
import com.konai.vam.core.util.PageRequestUtil.toBasePageable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Repository
class VirtualAccountBankRepository(
    private val virtualAccountBankJpaRepository: VirtualAccountBankJpaRepository
) : VirtualAccountBankEntityAdapter {

    override fun save(entity: VirtualAccountBankEntity): VirtualAccountBankEntity {
        return virtualAccountBankJpaRepository.save(entity)
    }

    override fun findByBankCode(bankCode: String): VirtualAccountBankEntity {
        return virtualAccountBankJpaRepository.findByBankCode(bankCode).orElseThrow { ResourceNotFoundException(ErrorCode.VIRTUAL_ACCOUNT_BANK_NOT_FOUND) }
    }

    override fun findAllByPredicate(predicate: VirtualAccountBankPredicate): BasePageable<VirtualAccountBankEntity?> {
        return virtualAccountBankJpaRepository.findPage(
            pageable = PageRequest.of(0, 1000),
            init = predicate.generateQuery()
        ).toBasePageable()
    }

}