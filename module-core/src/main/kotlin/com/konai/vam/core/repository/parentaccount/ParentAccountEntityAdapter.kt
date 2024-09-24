package com.konai.vam.core.repository.parentaccount

import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate

interface ParentAccountEntityAdapter {

    fun save(entity: ParentAccountEntity): ParentAccountEntity

    fun findByPredicate(predicate: ParentAccountPredicate): ParentAccountEntity?

    fun delete(id: Long)

    fun checkDuplicated(parentAccountNo: String, bankCode: String): Boolean

    fun findAllByPredicate(predicate: ParentAccountPredicate, pageableRequest: PageableRequest): BasePageable<ParentAccountEntity?>

}