package com.konai.vam.core.repository.parentaccount

import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity
import com.konai.vam.core.repository.parentaccount.jdsl.ParentAccountPredicate
import java.util.*

interface ParentAccountEntityAdapter {

    fun saveAll(entities: List<ParentAccountEntity>): List<ParentAccountEntity>

    fun findByPredicate(predicate: ParentAccountPredicate): Optional<ParentAccountEntity>

    fun delete(id: Long)

}