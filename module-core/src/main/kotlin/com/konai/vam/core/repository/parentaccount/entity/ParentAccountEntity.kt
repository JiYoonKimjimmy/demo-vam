package com.konai.vam.core.repository.parentaccount.entity

import com.konai.vam.core.common.converter.EncryptionCustomerInfoConverter
import jakarta.persistence.*

@Table(name = "VAM_MACN_LIST")
@Entity(name = "ParentAccount")
class ParentAccountEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VAM_MACN_LIST_SNO_SEQ")
    @SequenceGenerator(name = "VAM_MACN_LIST_SNO_SEQ", sequenceName = "VAM_MACN_LIST_SNO_SEQ", allocationSize = 1)
    @Column(name = "VAM_MACN_LIST_SNO")
    var id: Long? = null,

    @Convert(converter = EncryptionCustomerInfoConverter::class)
    @Column(name = "ENC_MACN_NO")
    val parentAccountNo: String,

    @Column(name = "BANK_CD")
    val bankCode: String

)