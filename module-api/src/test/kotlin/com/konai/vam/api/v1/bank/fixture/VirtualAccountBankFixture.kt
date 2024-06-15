package com.konai.vam.api.v1.bank.fixture

import com.konai.vam.api.v1.bank.service.domain.VirtualAccountBank

class VirtualAccountBankFixture {

    fun getDomain(id: Long? = null): VirtualAccountBank {
        return VirtualAccountBank(
            bankCode = "020",
            bankName = "우리 은행",
            bankEngName = "Woori Bank",
            rechargerId = "rechargerId",
        )
    }

}