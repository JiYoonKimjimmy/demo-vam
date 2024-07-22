package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccountMapper
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
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

    private fun findByPredicate(predicate: VirtualAccountPredicate): VirtualAccount {
        return virtualAccountEntityAdapter.findByPredicate(predicate)
            .orElseThrow { ResourceNotFoundException(ErrorCode.VIRTUAL_ACCOUNT_NOT_FOUND) }
            .let { virtualAccountMapper.entityToDomain(it) }
    }

}