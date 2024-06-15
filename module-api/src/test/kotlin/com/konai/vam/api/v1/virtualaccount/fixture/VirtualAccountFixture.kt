package com.konai.vam.api.v1.virtualaccount.fixture

import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.DISCONNECTED
import com.konai.vam.core.enumerate.VirtualAccountConnectType.FIXATION
import com.konai.vam.core.enumerate.VirtualAccountStatus.ACTIVE
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity

class VirtualAccountFixture {

    fun getDomain(id: Long? = null): VirtualAccount {
        return VirtualAccount(
            id = id,
            accountNo = "accountNo",
            bankCode = "001",
            connectType = FIXATION,
            status = ACTIVE,
            par = "par",
            cardConnectStatus = DISCONNECTED,
            cardSeBatchId = "batchId",
        )
    }

    fun getEntity(id: Long? = null): VirtualAccountEntity {
        return VirtualAccountEntity(
            id = id,
            accountNo = "accountNo",
            bankCode = "001",
            connectType = FIXATION,
            status = ACTIVE,
            par = "par",
            cardConnectStatus = DISCONNECTED,
            cardSeBatchId = "batchId",
        )
    }

}