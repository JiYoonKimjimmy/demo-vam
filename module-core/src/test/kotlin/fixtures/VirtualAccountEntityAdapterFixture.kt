package fixtures

import com.konai.vam.core.common.ifNotNullEquals
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.common.model.PageableRequest
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus.DISCONNECTED
import com.konai.vam.core.enumerate.VirtualAccountStatus
import com.konai.vam.core.repository.virtualaccount.VirtualAccountEntityAdapter
import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import com.konai.vam.core.repository.virtualaccount.jdsl.VirtualAccountPredicate
import fixtures.TestExtensionFunctions.generateSequence
import java.util.*

class VirtualAccountEntityAdapterFixture : VirtualAccountEntityAdapter {

    val entities = mutableListOf<VirtualAccountEntity>()

    private val virtualAccountEntityFixture = VirtualAccountEntityFixture()

    fun save(
        accountNo: String,
        status: VirtualAccountStatus,
        par: String? = null,
        serviceId: String? = null,
        cardConnectStatus: VirtualAccountCardConnectStatus = DISCONNECTED
    ): VirtualAccountEntity {
        val entity = virtualAccountEntityFixture.make(
            accountNo = accountNo,
            status = status,
            par = par,
            serviceId = serviceId,
            cardConnectStatus = cardConnectStatus
        )
        return save(entity)
    }

    fun clear() {
        entities.clear()
    }

    override fun saveAll(entities: List<VirtualAccountEntity>): List<VirtualAccountEntity> {
        return entities
    }

    override fun save(entity: VirtualAccountEntity): VirtualAccountEntity {
        deleteDuplicated(entity)
        entities += entity.apply { this.id = generateSequence() }
        return entity
    }

    override fun findById(id: Long, afterProc: ((Optional<VirtualAccountEntity>) -> VirtualAccountEntity)?): VirtualAccountEntity {
        return entities.first()
    }

    override fun findByPredicate(predicate: VirtualAccountPredicate): Optional<VirtualAccountEntity> {
        return entities
            .find { it.checkByPredicate(predicate) }
            .let { Optional.ofNullable(it) }
    }

    override fun findAllByPredicate(predicate: VirtualAccountPredicate, pageableRequest: PageableRequest): BasePageable<VirtualAccountEntity?> {
        return BasePageable(content = entities)
    }

    override fun existsByAccountNoAndBankCode(accountNo: String, bankCode: String): Boolean {
        return entities.any { it.accountNo == accountNo && it.bankCode == bankCode }
    }

    override fun existsByPars(pars: List<String>): Boolean {
        return entities.any { it.par in pars }
    }

    override fun existsByConnectStatusAndBatchId(connectStatus: VirtualAccountCardConnectStatus, batchId: String): Boolean {
        return entities.any {
            it.cardConnectStatus == connectStatus && it.cardSeBatchId == batchId
        }
    }

    private fun deleteDuplicated(entity: VirtualAccountEntity) {
        entities.removeIf { it.id == entity.id || (it.accountNo == entity.accountNo && it.bankCode == entity.bankCode) }
    }

    private fun VirtualAccountEntity.checkByPredicate(predicate: VirtualAccountPredicate): Boolean {
        return ifNotNullEquals(predicate.accountNo, this.accountNo)
                && ifNotNullEquals(predicate.bankCode, this.bankCode)
                && ifNotNullEquals(predicate.connectType, this.connectType)
                && ifNotNullEquals(predicate.status, this.status)
                && ifNotNullEquals(predicate.par, this.par)
                && ifNotNullEquals(predicate.serviceId, this.serviceId)
                && ifNotNullEquals(predicate.cardConnectStatus, this.cardConnectStatus)
    }

}