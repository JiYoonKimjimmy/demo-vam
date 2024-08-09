package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.DISCONNECTED
import com.konai.vam.core.enumerate.VirtualAccountConnectType.FIXATION
import com.konai.vam.core.enumerate.VirtualAccountStatus.ACTIVE
import com.konai.vam.core.repository.virtualaccount.VirtualAccountEntityAdapter
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import org.springframework.stereotype.Service

@Service
class VirtualAccountFindService(
    private val virtualAccountEntityAdapter: VirtualAccountEntityAdapter,
    private val virtualAccountMapper: VirtualAccountMapper,
) : VirtualAccountFindAdapter {

    override fun findCardConnectedVirtualAccount(accountNo: String): VirtualAccount {
        return findByPredicate(predicate = VirtualAccountPredicate(accountNo = accountNo, status = ACTIVE)).checkConnected()
    }

    override fun existsByPars(pars: List<String>): Boolean {
        return virtualAccountEntityAdapter.existsByPars(pars)
    }

    override fun existsByConnectStatusAndBatchId(connectStatus: VirtualAccountCardConnectStatus, batchId: String): Boolean {
        return virtualAccountEntityAdapter.existsByConnectStatusAndBatchId(connectStatus, batchId)
    }

    override fun findAllConnectableVirtualAccounts(bankCode: String, size: Int): List<VirtualAccount> {
        val predicate = VirtualAccountPredicate(bankCode = bankCode, connectType = FIXATION, status = ACTIVE, cardConnectStatus = DISCONNECTED)
        val pageable = PageableRequest(number = 0, size)
        return findAllByPredicate(predicate, pageable).content
    }

    private fun findByPredicate(predicate: VirtualAccountPredicate): VirtualAccount {
        return virtualAccountEntityAdapter.findByPredicate(predicate)
            .orElseThrow { ResourceNotFoundException(ErrorCode.VIRTUAL_ACCOUNT_NOT_FOUND) }
            .let { virtualAccountMapper.entityToDomain(it) }
    }

    private fun findAllByPredicate(predicate: VirtualAccountPredicate, pageable: PageableRequest): BasePageable<VirtualAccount> {
        return virtualAccountEntityAdapter.findAllByPredicate(predicate, pageable)
            .let { virtualAccountMapper.entitiesToPageable(it) }
    }

}