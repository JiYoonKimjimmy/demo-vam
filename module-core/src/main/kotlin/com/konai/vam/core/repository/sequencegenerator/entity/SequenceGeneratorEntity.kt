package com.konai.vam.core.repository.sequencegenerator.entity

import com.konai.vam.core.common.entity.BaseEntity
import com.konai.vam.core.enumerate.SequenceGeneratorType
import jakarta.persistence.*

@Table(name = "VAM_SNO_CRE_LIST")
@Entity(name = "SequenceGenerator")
class SequenceGeneratorEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SNO_CRE_LIST_SNO_SEQ")
    @SequenceGenerator(name = "SNO_CRE_LIST_SNO_SEQ", sequenceName = "SNO_CRE_LIST_SNO_SEQ", allocationSize = 1)
    @Column(name = "VAM_SNO_CRE_LIST_SNO")
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "VAM_SNO_CRE_DV_CD")
    val type: SequenceGeneratorType,

    @Column(name = "SNO_CRE_DT")
    val date: String,

    @Column(name = "SNO_CRE_CRNT_VAL")
    var value: Long

) : BaseEntity() {

    fun increment(): SequenceGeneratorEntity {
        this.value += 1
        return this
    }

}