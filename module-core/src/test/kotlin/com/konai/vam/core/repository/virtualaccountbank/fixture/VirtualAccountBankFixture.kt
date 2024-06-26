package com.konai.vam.core.repository.virtualaccountbank.fixture

import com.konai.vam.core.repository.virtualaccountbank.entity.VirtualAccountBankEntity

class VirtualAccountBankFixture {

    fun getEntity(): VirtualAccountBankEntity {
        return VirtualAccountBankEntity(
            bankCode = "020",
            bankName = "우리은행",
            bankEngName = "Woori Bank",
            rechargerId = "rechargerId",
        )
    }

}