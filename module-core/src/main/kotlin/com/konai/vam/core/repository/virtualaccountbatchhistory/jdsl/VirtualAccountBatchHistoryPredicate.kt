package com.konai.vam.core.repository.virtualaccountbatchhistory.jdsl

import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.repository.virtualaccountbatchhistory.entity.VirtualAccountBatchHistoryEntity
import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.JpqlQueryable
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery

class VirtualAccountBatchHistoryPredicate(
    val filePath: String? = null,
    val cardSeBatchId: String? = null,
    val result: Result? = null
) {

    fun generateQuery(): Jpql.() -> JpqlQueryable<SelectQuery<VirtualAccountBatchHistoryEntity>> {
        val query: (Jpql.() -> JpqlQueryable<SelectQuery<VirtualAccountBatchHistoryEntity>>) = {
            select(entity(VirtualAccountBatchHistoryEntity::class))
                .from(entity(VirtualAccountBatchHistoryEntity::class))
                .whereAnd(
                    this@VirtualAccountBatchHistoryPredicate.filePath       ?.let { path(VirtualAccountBatchHistoryEntity::filePath).eq(it) },
                    this@VirtualAccountBatchHistoryPredicate.cardSeBatchId  ?.let { path(VirtualAccountBatchHistoryEntity::cardSeBatchId).eq(it) },
                    this@VirtualAccountBatchHistoryPredicate.result         ?.let { path(VirtualAccountBatchHistoryEntity::result).eq(it) }
                )
        }
        return query
    }

}