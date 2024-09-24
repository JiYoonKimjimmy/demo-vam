package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount
import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccountMapper
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.repository.parentaccount.ParentAccountEntityAdapter
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import org.springframework.stereotype.Service

@Service
class ParentAccountFindService(
    private val parentAccountMapper: ParentAccountMapper,
    private val parentAccountEntityAdapter: ParentAccountEntityAdapter
) : ParentAccountFindAdapter {

    override fun findAll(predicate: ParentAccountPredicate): BasePageable<ParentAccount> {
        return parentAccountEntityAdapter.findAllByPredicate(predicate)
            .let { parentAccountMapper.entitiesToPageable(it) }
    }

}