package fixtures

import com.konai.vam.core.enumerate.SequenceGeneratorType
import com.konai.vam.core.repository.sequencegenerator.SequenceGeneratorEntityAdapter
import com.konai.vam.core.repository.sequencegenerator.entity.SequenceGeneratorEntity

class SequenceGeneratorEntityAdapterFixture : SequenceGeneratorEntityAdapter {

    override fun getNextSequence(type: SequenceGeneratorType, date: String): SequenceGeneratorEntity {
        return SequenceGeneratorEntity(
            type = type,
            date = date,
            value = 1
        )
    }

    override fun findSequence(type: SequenceGeneratorType, date: String): SequenceGeneratorEntity {
        return SequenceGeneratorEntity(
            type = type,
            date = date,
            value = 1
        )
    }

    override fun deleteSequence(type: SequenceGeneratorType, date: String) {

    }

}