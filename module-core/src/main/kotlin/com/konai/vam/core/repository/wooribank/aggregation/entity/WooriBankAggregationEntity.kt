package com.konai.vam.core.repository.wooribank.aggregation.entity

import com.konai.vam.core.common.entity.BaseEntity
import com.konai.vam.core.enumerate.WooriBankAggregateResult
import jakarta.persistence.*

@Table(name = "VAM_WR_BANK_TR_AGG")
@Entity(name = "WooriBankAggregation")
class WooriBankAggregationEntity(

    @Id
    @Column(name = "AGG_DT")
    val aggregateDate: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "AGG_RS_DV_CD")
    var aggregateResult: WooriBankAggregateResult?,

    @Column(name = "KN_DEPS_CCNT")
    val konaDepositCount: Int,

    @Column(name = "KN_DEPS_AMT")
    val konaDepositAmount: Long,

    @Column(name = "KN_DEPS_CNCL_CCNT")
    val konaDepositCancelCount: Int,

    @Column(name = "KN_DEPS_CNCL_AMT")
    val konaDepositCancelAmount: Long,

    @Column(name = "KN_PURE_DEPS_AMT")
    val konaDepositTrAmount: Long,

    @Column(name = "WR_BANK_DEPS_CCNT")
    var bankDepositCount: Int?,

    @Column(name = "WR_BANK_DEPS_AMT")
    var bankDepositAmount: Long?,

    @Column(name = "WR_BANK_DEPS_CNCL_CCNT")
    var bankDepositCancelCount: Int?,

    @Column(name = "WR_BANK_DEPS_CNCL_AMT")
    var bankDepositCancelAmount: Long?,

    @Column(name = "WR_BANK_PURE_DEPS_AMT")
    var bankDepositTrAmount: Long?

) : BaseEntity()