package com.konai.vam.core.repository.virtualaccountbatchhistory.entity

import com.konai.vam.core.common.entity.BaseEntity
import com.konai.vam.core.enumerate.Result
import jakarta.persistence.*

@Table(name = "VAM_VT_ACN_BAT_HIST")
@Entity(name = "VirtualAccountBatchHistory")
class VirtualAccountBatchHistoryEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VT_ACN_BAT_HIST_SNO_SEQ")
    @SequenceGenerator(name = "VT_ACN_BAT_HIST_SNO_SEQ", sequenceName = "VT_ACN_BAT_HIST_SNO_SEQ", allocationSize = 1)
    @Column(name = "VT_ACN_BAT_HIST_SNO")
    val id: Long? = null,

    @Column(name = "CRD_ISU_BAT_ID")
    val cardSeBatchId: String,

    @Column(name = "SVC_ID")
    val serviceId: String,

    @Column(name = "VT_ACN_BAT_QTY")
    val count: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "RS_ST_CD")
    val result: Result? = null,

    @Column(name = "FAIL_RSN_CN")
    val reason: String? = null,

    @Column(name = "FILE_PTH_NM")
    val filePath: String? = null

) : BaseEntity()