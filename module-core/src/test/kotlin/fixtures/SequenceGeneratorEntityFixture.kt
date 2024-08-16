package fixtures

import com.konai.vam.core.enumerate.SequenceGeneratorType
import com.konai.vam.core.repository.sequencegenerator.entity.SequenceGeneratorEntity
import com.konai.vam.core.util.convertPatternOf
import java.security.SecureRandom
import java.time.LocalDate

class SequenceGeneratorEntityFixture {

    val entities = mutableListOf<SequenceGeneratorEntity>()

    fun make(
        id: Long? = SecureRandom().nextLong(),
        type: SequenceGeneratorType = SequenceGeneratorType.WR_BANK,
        date: String = LocalDate.now().convertPatternOf(),
        value: Long = 0
    ): SequenceGeneratorEntity {
        return SequenceGeneratorEntity(
            id = id,
            date = date,
            type = type,
            value = value
        )
    }

    fun save(entity: SequenceGeneratorEntity): SequenceGeneratorEntity {
        deleteDuplicated(entity.date, entity.type)
        entities += entity
        return entity
    }

    private fun deleteDuplicated(date: String, type: SequenceGeneratorType) {
        entities.removeIf { it.date == date && it.type == type }
    }

}