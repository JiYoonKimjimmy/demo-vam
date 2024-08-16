package com.konai.vam.core.repository.sequencegenerator

import com.konai.vam.core.enumerate.SequenceGeneratorType
import com.konai.vam.core.repository.sequencegenerator.entity.SequenceGeneratorEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface SequenceGeneratorJpaRepository : JpaRepository<SequenceGeneratorEntity, Long> {

    @Query("SELECT s FROM SequenceGenerator s WHERE s.date = :date AND s.type = :type")
    fun findByTypeAndDate(type: SequenceGeneratorType, date: String): Optional<SequenceGeneratorEntity>

    fun deleteByTypeAndDate(type: SequenceGeneratorType, date: String)

}