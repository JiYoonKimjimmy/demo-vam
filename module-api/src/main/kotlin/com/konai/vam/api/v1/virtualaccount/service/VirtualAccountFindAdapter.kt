package com.konai.vam.api.v1.virtualaccount.service

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus

interface VirtualAccountFindAdapter {

    fun findCardConnectedVirtualAccount(accountNo: String): VirtualAccount

    fun existsByPars(pars: List<String>): Boolean

    fun existsByConnectStatusAndBatchId(connectStatus: VirtualAccountCardConnectStatus, batchId: String): Boolean

    fun findAllConnectableVirtualAccounts(bankCode: String, size: Int): List<VirtualAccount>

}