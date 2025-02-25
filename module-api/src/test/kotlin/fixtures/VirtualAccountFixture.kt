package fixtures

import com.konai.vam.api.v1.parentaccount.service.domain.ParentAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.BankAccount
import com.konai.vam.api.v1.virtualaccount.service.domain.VirtualAccount
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.DISCONNECTED
import com.konai.vam.core.enumerate.VirtualAccountConnectType.FIXATION
import com.konai.vam.core.enumerate.VirtualAccountStatus.ACTIVE
import fixtures.TestExtensionFunctions.generateUUID

class VirtualAccountFixture {

    fun make(
        id: Long? = null,
        bankAccount: BankAccount = BankAccount("020", "1234567890"),
        parentAccount: ParentAccount = ParentAccount(id, generateUUID(), bankAccount.bankCode)
    ): VirtualAccount {
        return VirtualAccount(
                id = id,
                bankAccount = bankAccount,
                connectType = FIXATION,
                status = ACTIVE,
                par = "par",
                serviceId = "serviceId",
                cardConnectStatus = DISCONNECTED,
                cardSeBatchId = "batchId",
            )
            .setParentAccount { parentAccount }
    }

}