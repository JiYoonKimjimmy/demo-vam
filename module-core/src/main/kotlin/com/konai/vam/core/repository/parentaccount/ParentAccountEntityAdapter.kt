package com.konai.vam.core.repository.parentaccount

import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import java.util.*

interface ParentAccountEntityAdapter {

    fun save(entity: ParentAccountEntity): ParentAccountEntity

    fun findByPredicate(predicate: ParentAccountPredicate): Optional<ParentAccountEntity>

    fun delete(id: Long)

    fun checkDuplicated(entity: ParentAccountEntity): ParentAccountEntity

    fun findAllByPredicate(predicate: ParentAccountPredicate, pageableRequest: PageableRequest): BasePageable<ParentAccountEntity?>

}