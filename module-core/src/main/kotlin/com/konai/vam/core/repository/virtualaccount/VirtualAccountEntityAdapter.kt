package com.konai.vam.core.repository.virtualaccount

import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import java.util.*

interface VirtualAccountEntityAdapter {

    fun save(entity: VirtualAccountEntity): VirtualAccountEntity

    fun saveAll(entities: List<VirtualAccountEntity>): List<VirtualAccountEntity>

    fun findById(id: Long, afterProc: ((Optional<VirtualAccountEntity>) -> VirtualAccountEntity)? = null): VirtualAccountEntity

    fun findByPredicate(predicate: VirtualAccountPredicate): Optional<VirtualAccountEntity>

    fun findAllByPredicate(predicate: VirtualAccountPredicate, pageableRequest: PageableRequest): BasePageable<VirtualAccountEntity?>

    fun existsByPars(pars: List<String>): Boolean

    fun existsByConnectStatusAndBatchId(connectStatus: VirtualAccountCardConnectStatus, batchId: String): Boolean

}