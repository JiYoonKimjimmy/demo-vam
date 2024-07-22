package com.konai.vam.core.repository.wooribank.management.jdsl

import com.konai.vam.core.enumerate.WooriBankResponseCode
import com.konai.vam.core.repository.wooribank.management.entity.WooriBankManagementEntity
import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.JpqlQueryable
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery

data class WooriBankManagementPredicate(
    val messageTypeCode: String? = null,
    val businessTypeCode: String? = null,
    val messageNo: String? = null,
    val transmissionDate: String? = null,
    val responseCode: WooriBankResponseCode? = null,
) {

    fun generateQuery(): Jpql.() -> JpqlQueryable<SelectQuery<WooriBankManagementEntity>> {
        val query: (Jpql.() -> JpqlQueryable<SelectQuery<WooriBankManagementEntity>>) = {
            select(entity(WooriBankManagementEntity::class))
                .from(entity(WooriBankManagementEntity::class))
                .whereAnd(
                    this@WooriBankManagementPredicate.messageTypeCode  ?.let { path(WooriBankManagementEntity::messageTypeCode).eq(it) },
                    this@WooriBankManagementPredicate.businessTypeCode ?.let { path(WooriBankManagementEntity::businessTypeCode).eq(it) },
                    this@WooriBankManagementPredicate.messageNo        ?.let { path(WooriBankManagementEntity::messageNo).eq(it) },
                    this@WooriBankManagementPredicate.transmissionDate ?.let { path(WooriBankManagementEntity::transmissionDate).eq(it) },
                    this@WooriBankManagementPredicate.responseCode     ?.let { path(WooriBankManagementEntity::responseCode).eq(it) },
                )
        }
        return query
    }

}