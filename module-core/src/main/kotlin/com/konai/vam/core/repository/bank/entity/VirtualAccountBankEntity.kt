package com.konai.vam.core.repository.bank.entity

import com.konai.vam.core.common.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity(name = "VIRTUAL_ACCOUNT_BANKS")
class VirtualAccountBankEntity(

    @Id
    val bankCode: String,
    val bankName: String,
    val bankEngName: String,
    val rechargerId: String,

) : BaseEntity()