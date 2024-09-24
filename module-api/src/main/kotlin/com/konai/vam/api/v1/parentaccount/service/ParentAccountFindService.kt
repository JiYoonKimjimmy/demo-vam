package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount
import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccountMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.parentaccount.ParentAccountEntityAdapter
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import org.springframework.stereotype.Service

@Service
class ParentAccountFindService(
    private val parentAccountMapper: ParentAccountMapper,
    private val parentAccountEntityAdapter: ParentAccountEntityAdapter
) : ParentAccountFindAdapter {

    override fun findOne(predicate: ParentAccountPredicate): ParentAccount {
        return parentAccountEntityAdapter.findByPredicate(predicate)
            .takeIf { it != null }
            ?.let { parentAccountMapper.entityToDomain(it) }
            ?: throw ResourceNotFoundException(ErrorCode.PARENT_ACCOUNT_NOT_FOUND)
    }

    override fun findAll(predicate: ParentAccountPredicate): BasePageable<ParentAccount> {
        return parentAccountEntityAdapter.findAllByPredicate(predicate, PageableRequest(size = 1000))
            .let { parentAccountMapper.entitiesToPageable(it) }
    }

}