package fixtures

import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus
import com.konai.vam.core.enumerate.VirtualAccountConnectType
import com.konai.vam.core.enumerate.VirtualAccountConnectType.FIXATION
import com.konai.vam.core.enumerate.VirtualAccountStatus
import com.konai.vam.core.enumerate.VirtualAccountStatus.ACTIVE
import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import fixtures.TestExtensionFunctions.generateUUID
import java.security.SecureRandom

class VirtualAccountEntityFixture {

    val entities = mutableListOf<VirtualAccountEntity>()

    fun make(
        id: Long? = SecureRandom().nextLong(),
        accountNo: String = generateUUID(14),
        bankCode: String = "020",
        connectType: VirtualAccountConnectType = FIXATION,
        status: VirtualAccountStatus = ACTIVE,
        par: String? = null,
        serviceId: String? = null,
        cardConnectStatus: VirtualAccountCardConnectStatus = VirtualAccountCardConnectStatus.DISCONNECTED,
        cardSeBatchId: String? = null,
        parentAccount: ParentAccountEntity? = null
    ): VirtualAccountEntity {
        return VirtualAccountEntity(
            id = id,
            accountNo = accountNo,
            bankCode = bankCode,
            connectType = connectType,
            status = status,
            par = par,
            serviceId = serviceId,
            cardConnectStatus = cardConnectStatus,
            cardSeBatchId = cardSeBatchId,
            parentAccount = parentAccount ?: ParentAccountEntity(id = SecureRandom().nextLong(), parentAccountNo = generateUUID(14), bankCode = bankCode)
        )
    }

    fun save(entity: VirtualAccountEntity): VirtualAccountEntity {
        deleteDuplicated(entity.accountNo, entity.bankCode)
        entities += entity
        return entity
    }

    private fun deleteDuplicated(accountNo: String, bankCode: String) {
        entities.removeIf { it.accountNo == accountNo && it.bankCode == bankCode }
    }

}