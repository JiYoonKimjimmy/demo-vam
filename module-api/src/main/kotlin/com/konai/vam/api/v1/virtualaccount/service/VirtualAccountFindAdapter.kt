package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate

interface VirtualAccountFindAdapter {

    fun findByPredicate(predicate: VirtualAccountPredicate): VirtualAccount

    fun findAllByPredicate(predicate: VirtualAccountPredicate, pageable: PageableRequest): BasePageable<VirtualAccount>

    fun findCardConnectedVirtualAccount(accountNo: String): VirtualAccount

    fun findAllConnectableVirtualAccounts(bankCode: String, size: Int): List<VirtualAccount>

    fun existsByPars(pars: List<String>): Boolean

    fun existsByConnectStatusAndBatchId(connectStatus: VirtualAccountCardConnectStatus, batchId: String): Boolean

}