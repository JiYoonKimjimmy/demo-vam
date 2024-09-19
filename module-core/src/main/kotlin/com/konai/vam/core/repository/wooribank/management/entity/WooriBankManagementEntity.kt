package com.konai.vam.core.repository.wooribank.management.entity

import com.konai.vam.core.common.converter.EncryptionCustomerInfoConverter
import com.konai.vam.core.common.entity.BaseEntity
import com.konai.vam.core.enumerate.WooriBankResponseCode
import com.konai.vam.core.enumerate.YesOrNo
import jakarta.persistence.*

@Table(name = "VAM_WR_BANK_GM_LNK_HIST")
@Entity(name = "WooriBankManagement")
class WooriBankManagementEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WR_BANK_GM_LNK_HIST_SNO_SEQ")
    @SequenceGenerator(name = "WR_BANK_GM_LNK_HIST_SNO_SEQ", sequenceName = "WR_BANK_GM_LNK_HIST_SNO_SEQ", allocationSize = 1)
    @Column(name = "WR_BANK_GM_LNK_HIST_SNO")
    var id: Long? = null,

    @Column(name = "WR_BANK_DCRM_TYPE_CD")
    val identifierCode: String,

    @Column(name = "WR_BANK_ENP_TYPE_CD")
    val companyNo: String,

    @Column(name = "WR_BANK_OGN_TYPE_CD")
    val institutionCode: String,

    @Column(name = "WR_BANK_GM_TYPE_CD")
    val messageTypeCode: String,

    @Column(name = "WR_BANK_BS_TYPE_CD")
    val businessTypeCode: String,

    @Column(name = "TRSF_TMCNT")
    val transmissionCount: Int,

    @Column(name = "WR_BANK_GMNO")
    val messageNo: String,

    @Column(name = "TRSF_DT")
    val transmissionDate: String,

    @Column(name = "TRSF_TM")
    val transmissionTime: String,

    @Column(name = "WR_BANK_RSP_CD")
    @Enumerated(EnumType.STRING)
    val responseCode: WooriBankResponseCode?,

    @Column(name = "WR_BANK_ORG_GMNO")
    val orgMessageNo: String?,

    @Convert(converter = EncryptionCustomerInfoConverter::class)
    @Column(name = "ENC_MACN_NO")
    val parentAccountNo: String,

    @Column(name = "TR_DT")
    val trDate: String,

    @Column(name = "TR_TI")
    val trTime: String,

    @Column(name = "TID")
    val tid: String,

    @Column(name = "WR_BANK_TR_MDI_TYPE_CD")
    val trMedium: String,

    @Column(name = "WR_BANK_TR_AMT")
    val trAmount: Long,

    @Column(name = "OBR_CSCK_AMT")
    val otherCashierCheckAmount: Long,

    @Column(name = "ETC_OBR_CSCK_AMT")
    val etcOtherCashierCheckAmount: Long,

    @Column(name = "WR_BANK_TR_BRCH_TYPE_CD")
    val trBranch: String,

    @Convert(converter = EncryptionCustomerInfoConverter::class)
    @Column(name = "ENC_DEPSP_NM")
    val depositorName: String?,

    @Convert(converter = EncryptionCustomerInfoConverter::class)
    @Column(name = "ENC_VT_ACNO")
    val accountNo: String,

    @Column(name = "VT_ACN_NM")
    val accountName: String,

    @Column(name = "VT_ACN_BALA")
    val accountBalance: Long,

    @Column(name = "CSH_DEPS_YN")
    val cashDepositYn: String?,

    @Column(name = "WR_BANK_CSCK_AMT")
    val cashierCheckAmount: Long,

    @Column(name = "WR_BANK_BRCH_TYPE_CD")
    val branchCode: String?,

    @Column(name = "DEPS_CFM_NOTI_YN")
    @Enumerated(EnumType.STRING)
    val depositConfirm: YesOrNo = YesOrNo.N,

    @Column(name = "RSP_MSG_CN")
    val responseMessage: String?

) : BaseEntity()