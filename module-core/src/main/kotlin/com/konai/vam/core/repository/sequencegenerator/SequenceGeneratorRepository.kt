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
    override fun getNextSequence(date: String, type: SequenceGeneratorType): SequenceGeneratorEntity {
        return findSequence(date, type)
            .increment()
            .let(this::save)
    }

    private fun findSequence(date: String, type: SequenceGeneratorType): SequenceGeneratorEntity {
        return sequenceGeneratorJpaRepository
            .findByDateAndType(date, type)
            .orElse(SequenceGeneratorEntity(date = date, type = type, value = 0))
    }

    private fun save(entity: SequenceGeneratorEntity): SequenceGeneratorEntity {
        return sequenceGeneratorJpaRepository.save(entity)
    }

}