package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount
import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccountMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.repository.parentaccount.ParentAccountEntityAdapter
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import org.springframework.stereotype.Service

@Service
class ParentAccountManagementService(
    private val parentAccountMapper: ParentAccountMapper,
    private val parentAccountEntityAdapter: ParentAccountEntityAdapter
) : ParentAccountManagementAdapter {

    override fun save(domain: ParentAccount): ParentAccount {
        return if (parentAccountEntityAdapter.checkDuplicated(domain.parentAccountNo, domain.bankCode)) {
            throw InternalServiceException(ErrorCode.PARENT_ACCOUNT_IS_DUPLICATED)
        } else {
            parentAccountMapper.domainToEntity(domain)
                .let { parentAccountEntityAdapter.save(it) }
                .let { parentAccountMapper.entityToDomain(it) }
        }
    }

    override fun update(domain: ParentAccount): ParentAccount {
        return ParentAccountPredicate(id = domain.id)
            .let { parentAccountEntityAdapter.findByPredicate(it) }
            ?.let { parentAccountMapper.entityToDomain(it) }
            ?.updateParentAccountNoOrBankCode(domain.parentAccountNo, domain.bankCode)
            ?.let { this.save(it) }
            ?: throw ResourceNotFoundException(ErrorCode.PARENT_ACCOUNT_NOT_FOUND)
    }
}