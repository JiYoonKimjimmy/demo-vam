package com.konai.vam.core.repository.virtualaccountbank.jdsl

import com.konai.vam.core.repository.virtualaccountbank.entity.VirtualAccountBankEntity
import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.JpqlQueryable
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery

data class VirtualAccountBankPredicate(
    val bankCode: String? = null,
    val bankName: String? = null,
) {

    fun generateQuery(): Jpql.() -> JpqlQueryable<SelectQuery<VirtualAccountBankEntity>> {
        val query: (Jpql.() -> JpqlQueryable<SelectQuery<VirtualAccountBankEntity>>) = {
            select(entity(VirtualAccountBankEntity::class))
                .from(entity(VirtualAccountBankEntity::class))
                .whereAnd(
                    this@VirtualAccountBankPredicate.bankCode?.let { path(VirtualAccountBankEntity::bankCode).eq(it) },
                    this@VirtualAccountBankPredicate.bankName?.let { path(VirtualAccountBankEntity::bankName).eq(it) },
                )
        }
        return query
    }

}