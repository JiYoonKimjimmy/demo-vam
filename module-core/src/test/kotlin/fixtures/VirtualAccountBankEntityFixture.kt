package fixtures

import com.konai.vam.core.repository.virtualaccountbank.VirtualAccountBankConst
import com.konai.vam.core.repository.virtualaccountbank.entity.VirtualAccountBankEntity

class VirtualAccountBankEntityFixture {

    val entities = mutableListOf<VirtualAccountBankEntity>()

    fun make(
        const: VirtualAccountBankConst,
        rechargerId: String = "rechargerId"
    ): VirtualAccountBankEntity {
        return make(const.bankCode, const.bankName, const.bankEngName, rechargerId)
    }

    fun make(
        bankCode: String = VirtualAccountBankConst.woori.bankCode,
        bankName: String = VirtualAccountBankConst.woori.bankName,
        bankEngName: String = VirtualAccountBankConst.woori.bankEngName,
        rechargerId: String = "rechargerId"
    ): VirtualAccountBankEntity {
        return VirtualAccountBankEntity(
            bankCode = bankCode,
            bankName = bankName,
            bankEngName = bankEngName,
            rechargerId = rechargerId,
        )
    }

    fun save(entity: VirtualAccountBankEntity): VirtualAccountBankEntity {
        deleteDuplicated(entity.bankCode)
        entities += entity
        return entity
    }

    private fun deleteDuplicated(bankCode: String) {
        entities.removeIf { it.bankCode == bankCode }
    }

}