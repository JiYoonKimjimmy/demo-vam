package fixtures

import com.konai.vam.core.common.ifNotNullEquals
import com.konai.vam.core.enumerate.WooriBankMessageType
import com.konai.vam.core.enumerate.WooriBankResponseCode
import com.konai.vam.core.repository.wooribank.management.WooriBankManagementEntityAdapter
import com.konai.vam.core.repository.wooribank.management.entity.WooriBankManagementEntity
import com.konai.vam.core.repository.wooribank.management.jdsl.WooriBankManagementPredicate
import com.konai.vam.core.util.DATE_yyMMdd_PATTERN
import com.konai.vam.core.util.convertPatternOf
import java.time.LocalDate
import java.util.*

class WooriBankManagementEntityAdapterFixture : WooriBankManagementEntityAdapter {

    private val wooriBankManagementEntityFixture = WooriBankManagementEntityFixture()

    fun save(
        messageCode: WooriBankMessageType.Code,
        messageNo: String,
        transmissionDate: String = LocalDate.now().convertPatternOf(DATE_yyMMdd_PATTERN),
        responseCode: WooriBankResponseCode? = null
    ): WooriBankManagementEntity {
        return wooriBankManagementEntityFixture.save(messageCode, messageNo, transmissionDate, responseCode)
    }

    override fun save(entity: WooriBankManagementEntity): WooriBankManagementEntity {
        return wooriBankManagementEntityFixture.save(entity)
    }

    override fun findByPredicate(predicate: WooriBankManagementPredicate): Optional<WooriBankManagementEntity> {
        return wooriBankManagementEntityFixture.entities
            .find {
                ifNotNullEquals(predicate.messageTypeCode, it.messageTypeCode)
                && ifNotNullEquals(predicate.businessTypeCode, it.businessTypeCode)
                && ifNotNullEquals(predicate.messageNo, it.messageNo)
                && ifNotNullEquals(predicate.responseCode, it.responseCode)
                && ifNotNullEquals(predicate.transmissionDate, it.transmissionDate)
            }
            .let { Optional.ofNullable(it) }
    }

}