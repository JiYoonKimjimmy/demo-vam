package com.konai.vam.core.repository.sequencegenerator

import com.konai.vam.core.enumerate.SequenceGeneratorType
import com.konai.vam.core.repository.sequencegenerator.entity.SequenceGeneratorEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class SequenceGeneratorRepository(
    private val sequenceGeneratorJpaRepository: SequenceGeneratorJpaRepository
) : SequenceGeneratorEntityAdapter {

    @Transactional
    override fun getNextSequence(type: SequenceGeneratorType, date: String): SequenceGeneratorEntity {
        return findSequence(type, date)
            .increment()
            .let(this::save)
    }

    private fun findSequence(type: SequenceGeneratorType, date: String): SequenceGeneratorEntity {
        return sequenceGeneratorJpaRepository
            .findByTypeAndDate(type, date)
            .orElse(SequenceGeneratorEntity(type = type, date = date, value = 0))
    }

    private fun save(entity: SequenceGeneratorEntity): SequenceGeneratorEntity {
        return sequenceGeneratorJpaRepository.save(entity)
    }

}