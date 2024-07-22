package fixtures

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.ResourceNotFoundException
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.repository.virtualaccountbank.VirtualAccountBankConst
import com.konai.vam.core.repository.virtualaccountbank.VirtualAccountBankEntityAdapter
import com.konai.vam.core.repository.virtualaccountbank.entity.VirtualAccountBankEntity
import com.konai.vam.core.repository.virtualaccountbank.jdsl.VirtualAccountBankPredicate

class VirtualAccountBankEntityAdapterFixture : VirtualAccountBankEntityAdapter {

    private val virtualAccountBankEntityFixture = VirtualAccountBankEntityFixture()

    fun save(const: VirtualAccountBankConst, rechargerId: String = "rechargerId"): VirtualAccountBankEntity {
        val entity = virtualAccountBankEntityFixture.make(bankCode = const.bankCode, bankName = const.bankName, bankEngName = const.bankEngName, rechargerId = rechargerId)
        return save(entity)
    }

    override fun save(entity: VirtualAccountBankEntity): VirtualAccountBankEntity {
        return virtualAccountBankEntityFixture.save(entity)
    }

    override fun findByBankCode(bankCode: String): VirtualAccountBankEntity {
        return virtualAccountBankEntityFixture.entities
            .find { it.bankCode == bankCode }
            ?: throw ResourceNotFoundException(ErrorCode.VIRTUAL_ACCOUNT_BANK_NOT_FOUND)
    }

    override fun findAllByPredicate(predicate: VirtualAccountBankPredicate): BasePageable<VirtualAccountBankEntity?> {
        return virtualAccountBankEntityFixture.entities
            .filter { e ->
                predicate.bankCode?.let { e.bankCode == it } ?: true
                && predicate.bankName?.let { e.bankName == it } ?: true
            }
            .let { BasePageable(pageable = BasePageable.Pageable(numberOfElements = it.size, totalElements = it.size), content = it) }
    }

}