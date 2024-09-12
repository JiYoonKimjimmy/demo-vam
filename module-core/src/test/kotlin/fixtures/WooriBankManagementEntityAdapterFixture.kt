package fixtures

import com.konai.vam.core.common.ifNotNullEquals
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.enumerate.WooriBankResponseCode
import com.konai.vam.core.repository.wooribank.management.WooriBankManagementEntityAdapter
import com.konai.vam.core.repository.wooribank.management.entity.WooriBankManagementEntity
import com.konai.vam.core.repository.wooribank.management.jdsl.WooriBankManagementPredicate
import com.konai.vam.core.util.DATE_yyMMdd_PATTERN
import com.konai.vam.core.util.convertPatternOf
import java.security.SecureRandom
import java.time.LocalDate
import java.util.*

class WooriBankManagementEntityAdapterFixture : WooriBankManagementEntityAdapter {

    private val wooriBankManagementEntityFixture = WooriBankManagementEntityFixture()
    private val entities = mutableListOf<WooriBankManagementEntity>()

    fun save(
        messageCode: WooriBankMessageType.Code,
        messageNo: String,
        transmissionDate: String = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN),
        responseCode: WooriBankResponseCode? = null
    ): WooriBankManagementEntity {
        val entity = wooriBankManagementEntityFixture.make(messageCode, messageNo, transmissionDate, responseCode)
        return save(entity)
    }

    override fun save(entity: WooriBankManagementEntity): WooriBankManagementEntity {
        entities.removeIf { it.id == entity.id }
        entities += entity
        return entity.apply { this.id = SecureRandom().nextLong() }
    }

    override fun findByPredicate(predicate: WooriBankManagementPredicate): Optional<WooriBankManagementEntity> {
        return entities
            .find {
                ifNotNullEquals(predicate.messageTypeCode, it.messageTypeCode)
                && ifNotNullEquals(predicate.businessTypeCode, it.businessTypeCode)
                && ifNotNullEquals(predicate.messageNo, it.messageNo)
                && ifNotNullEquals(predicate.responseCode, it.responseCode)
                && ifNotNullEquals(predicate.transmissionDate, it.transmissionDate)
            }
            .let { Optional.ofNullable(it) }
    }

    fun findAll(predicate: WooriBankManagementPredicate): List<WooriBankManagementEntity> {
        return entities
            .filter {
                ifNotNullEquals(predicate.messageTypeCode, it.messageTypeCode)
                && ifNotNullEquals(predicate.businessTypeCode, it.businessTypeCode)
                && ifNotNullEquals(predicate.messageNo, it.messageNo)
                && ifNotNullEquals(predicate.responseCode, it.responseCode)
                && ifNotNullEquals(predicate.transmissionDate, it.transmissionDate)
            }
    }

}