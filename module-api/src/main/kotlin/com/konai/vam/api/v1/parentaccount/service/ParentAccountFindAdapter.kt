package com.konai.vam.api.v1.parentaccount.service

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate

interface ParentAccountFindAdapter {

    fun findAll(predicate: ParentAccountPredicate): BasePageable<ParentAccount>

}