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
import java.util.*

class VirtualAccountEntityAdapterFixture : VirtualAccountEntityAdapter {

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

    override fun saveAll(entities: List<VirtualAccountEntity>): List<VirtualAccountEntity> {
        return virtualAccountEntityFixture.entities
    }

    override fun save(entity: VirtualAccountEntity): VirtualAccountEntity {
        return virtualAccountEntityFixture.save(entity)
    }

    override fun findById(id: Long, afterProc: ((Optional<VirtualAccountEntity>) -> VirtualAccountEntity)?): VirtualAccountEntity {
        return virtualAccountEntityFixture.entities.first()
    }

    override fun findByPredicate(predicate: VirtualAccountPredicate): Optional<VirtualAccountEntity> {
        return virtualAccountEntityFixture.entities
            .find {
                ifNotNullEquals(predicate.accountNo, it.accountNo)
                && ifNotNullEquals(predicate.bankCode, it.bankCode)
                && ifNotNullEquals(predicate.connectType, it.connectType)
                && ifNotNullEquals(predicate.status, it.status)
                && ifNotNullEquals(predicate.par, it.par)
                && ifNotNullEquals(predicate.serviceId, it.serviceId)
                && ifNotNullEquals(predicate.cardConnectStatus, it.cardConnectStatus)
            }
            .let { Optional.ofNullable(it) }
    }

    override fun findAllByPredicate(predicate: VirtualAccountPredicate, pageableRequest: PageableRequest): BasePageable<VirtualAccountEntity?> {
        return BasePageable(content = virtualAccountEntityFixture.entities)
    }

    override fun existsByPars(pars: List<String>): Boolean {
        return virtualAccountEntityFixture.entities.any { it.par in pars }
    }

}