package com.konai.vam.core.repository.parentaccount.jdsl

import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity
import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.JpqlQueryable
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery

data class ParentAccountPredicate(
    val id: Long? = null,
    val parentAccountNo: String? = null,
    val bankCode: String? = null
) {
    fun generateQuery(): Jpql.() -> JpqlQueryable<SelectQuery<ParentAccountEntity>> {
        val query: (Jpql.() -> JpqlQueryable<SelectQuery<ParentAccountEntity>>) = {
            select(entity(ParentAccountEntity::class))
                .from(entity(ParentAccountEntity::class))
                .whereAnd(
                    this@ParentAccountPredicate.id              ?.let { path(ParentAccountEntity::id).eq(it) },
                    this@ParentAccountPredicate.parentAccountNo ?.let { path(ParentAccountEntity::parentAccountNo).eq(it) },
                    this@ParentAccountPredicate.bankCode        ?.let { path(ParentAccountEntity::bankCode).eq(it) },
                )
        }
        return query
    }
}