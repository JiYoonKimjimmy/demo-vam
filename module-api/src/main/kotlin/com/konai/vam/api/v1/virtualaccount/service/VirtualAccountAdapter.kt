package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate

interface VirtualAccountAdapter {

    fun create(domain: VirtualAccount): VirtualAccount

    fun findOne(predicate: VirtualAccountPredicate): VirtualAccount

    fun findPage(predicate: VirtualAccountPredicate, pageable: PageableRequest): BasePageable<VirtualAccount>

}