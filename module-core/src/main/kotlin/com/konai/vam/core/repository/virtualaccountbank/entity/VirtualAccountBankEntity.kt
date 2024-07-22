package com.konai.vam.core.repository.virtualaccountbank.entity

import com.konai.vam.core.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "VAM_VT_ACN_BANK_LIST")
@Entity(name = "VirtualAccountBank")
class VirtualAccountBankEntity(

    @Id
    @Column(name = "VT_ACN_BANK_CD")
    val bankCode: String,

    @Column(name = "BANK_NM")
    val bankName: String,

    @Column(name = "BANK_ENM")
    val bankEngName: String,

    @Column(name = "RC_ID")
    val rechargerId: String

) : BaseEntity()