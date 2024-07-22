package com.konai.vam.core.repository.rechargetransaction.entity

import com.konai.vam.core.common.converter.EncryptionCardInfoConverter
import com.konai.vam.core.common.entity.BaseEntity
import com.konai.vam.core.enumerate.RechargeTransactionType
import com.konai.vam.core.enumerate.Result
import com.konai.vam.core.enumerate.RechargeTransactionCancelStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "VAM_CHRG_TR_HIST")
@Entity(name = "RechargeTransaction")
class RechargeTransactionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHRG_TR_HIST_SNO_SEQ")
    @SequenceGenerator(name = "CHRG_TR_HIST_SNO_SEQ", sequenceName = "CHRG_TR_HIST_SNO_SEQ", allocationSize = 1)
    @Column(name = "CHRG_TR_HIST_SNO")
    val id: Long? = null,

    @Column(name = "CHRG_TR_HIST_GMNO")
    val tranNo: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "CHRG_TR_DV_CD")
    val tranType: RechargeTransactionType,

    @Enumerated(EnumType.STRING)
    @Column(name = "RS_ST_CD")
    val result: Result,

    @Column(name = "CRD_CHRG_AMT")
    val amount: Long,

    @Convert(converter = EncryptionCardInfoConverter::class)
    @Column(name = "VT_ACNO")
    val accountNo: String,

    @Column(name = "BANK_CD")
    val bankCode: String,

    @Column(name = "PAR_NO")
    val par: String,

    @Column(name = "SVC_ID")
    val serviceId: String,

    @Column(name = "TRAN_ID")
    val transactionId: String?,

    @Column(name = "NR_NO")
    val nrNumber: String?,

    @Column(name = "RC_ID")
    val rechargerId: String?,

    @Column(name = "CHRG_RQS_DTTM")
    val rechargeDate: LocalDateTime?,

    @Enumerated(EnumType.STRING)
    @Column(name = "CHRG_TR_CNCL_ST_DV_CD")
    val cancelStatus: RechargeTransactionCancelStatus?,

    @Column(name = "CHRG_CNCL_DTTM")
    val cancelDate: LocalDateTime?,

    @Column(name = "FAIL_RSN_CN")
    val reason: String?

) : BaseEntity()