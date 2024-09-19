package com.konai.vam.core.repository.virtualaccount.entity

import com.konai.vam.core.common.converter.EncryptionCustomerInfoConverter
import com.konai.vam.core.common.entity.BaseEntity
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus
import com.konai.vam.core.enumerate.VirtualAccountConnectType
import com.konai.vam.core.enumerate.VirtualAccountStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "VAM_VT_ACN_LIST")
@Entity(name = "VirtualAccount")
class VirtualAccountEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VT_ACN_LIST_SNO_SEQ")
    @SequenceGenerator(name = "VT_ACN_LIST_SNO_SEQ", sequenceName = "VT_ACN_LIST_SNO_SEQ", allocationSize = 1)
    @Column(name = "VT_ACN_LIST_SNO")
    val id: Long? = null,

    @Convert(converter = EncryptionCustomerInfoConverter::class)
    @Column(name = "ENC_VT_ACNO")
    val accountNo: String,

    @Column(name = "BANK_CD")
    val bankCode: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "VT_ACN_CONN_DV_CD")
    val connectType: VirtualAccountConnectType,

    @Enumerated(EnumType.STRING)
    @Column(name = "VT_ACN_ST_DV_CD")
    val status: VirtualAccountStatus,

    @Column(name = "PAR_NO")
    val par: String? = null,

    @Column(name = "SVC_ID")
    val serviceId: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "VT_ACN_CRD_CONN_ST_DV_CD")
    val cardConnectStatus: VirtualAccountCardConnectStatus,

    @Column(name = "CONN_DTTM")
    val cardConnected: LocalDateTime? = null,

    @Column(name = "CONN_DSBL_DTTM")
    val cardDisconnected: LocalDateTime? = null,

    @Column(name = "CRD_ISU_BAT_ID")
    val cardSeBatchId: String? = null,

    @Convert(converter = EncryptionCustomerInfoConverter::class)
    @Column(name = "ENC_MACN_NO")
    val parentAccountNo: String? = null,

    ) : BaseEntity()