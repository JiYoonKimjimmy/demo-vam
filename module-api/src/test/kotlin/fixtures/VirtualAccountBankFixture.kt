package fixtures

import com.konai.vam.api.v1.virtualaccountbank.service.domain.VirtualAccountBank

class VirtualAccountBankFixture {

    fun getOne(): VirtualAccountBank {
        return VirtualAccountBank(
            bankCode = "020",
            bankName = "우리 은행",
            bankEngName = "Woori Bank",
            rechargerId = "rechargerId",
        )
    }

}