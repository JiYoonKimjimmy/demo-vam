package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount
import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccountMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.repository.parentaccount.ParentAccountEntityAdapter
import org.springframework.stereotype.Service

@Service
class ParentAccountSaveService(
    private val parentAccountMapper: ParentAccountMapper,
    private val parentAccountEntityAdapter: ParentAccountEntityAdapter
) : ParentAccountSaveAdapter {

    override fun save(domain: ParentAccount): ParentAccount {
        return if (parentAccountEntityAdapter.checkDuplicated(domain.parentAccountNo, domain.bankCode)) {
            throw InternalServiceException(ErrorCode.PARENT_ACCOUNT_IS_DUPLICATED)
        } else {
            parentAccountMapper.domainToEntity(domain)
                .let { parentAccountEntityAdapter.save(it) }
                .let { parentAccountMapper.entityToDomain(it) }
        }
    }

}