package com.konai.vam.core.repository.virtualaccount.jdsl

import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus
import com.konai.vam.core.enumerate.VirtualAccountConnectType
import com.konai.vam.core.enumerate.VirtualAccountStatus
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.JpqlQueryable
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery

data class VirtualAccountPredicate(
    val accountNo: String? = null,
    val bankCode: String? = null,
    val connectType: VirtualAccountConnectType? = null,
    val status: VirtualAccountStatus? = null,
    val par: String? = null,
    val serviceId: String? = null,
    val cardConnectStatus: VirtualAccountCardConnectStatus? = null,
    val cardSeBatchId: String? = null,
) {

    fun generateQuery(): Jpql.() -> JpqlQueryable<SelectQuery<VirtualAccountEntity>> {
        val query: (Jpql.() -> JpqlQueryable<SelectQuery<VirtualAccountEntity>>) = {
            select(entity(VirtualAccountEntity::class))
                .from(entity(VirtualAccountEntity::class))
                .whereAnd(
                    this@VirtualAccountPredicate.accountNo         ?.let { path(VirtualAccountEntity::accountNo).eq(it) },
                    this@VirtualAccountPredicate.bankCode          ?.let { path(VirtualAccountEntity::bankCode).eq(it) },
                    this@VirtualAccountPredicate.connectType       ?.let { path(VirtualAccountEntity::connectType).eq(it) },
                    this@VirtualAccountPredicate.status            ?.let { path(VirtualAccountEntity::status).eq(it) },
                    this@VirtualAccountPredicate.par               ?.let { path(VirtualAccountEntity::par).eq(it) },
                    this@VirtualAccountPredicate.serviceId         ?.let { path(VirtualAccountEntity::serviceId).eq(it) },
                    this@VirtualAccountPredicate.cardConnectStatus ?.let { path(VirtualAccountEntity::cardConnectStatus).eq(it) },
                    this@VirtualAccountPredicate.cardSeBatchId     ?.let { path(VirtualAccountEntity::cardSeBatchId).eq(it) },
                )
        }
        return query
    }

}