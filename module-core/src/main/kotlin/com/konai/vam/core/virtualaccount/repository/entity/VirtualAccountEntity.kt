package com.konai.vam.core.virtualaccount.repository.entity

import com.konai.vam.core.common.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "VIRTUAL_ACCOUNT")
class VirtualAccountEntity(

    @Id
    @GeneratedValue
    val id: Long? = null,
    val accountNumber: String,
    val bankCode: String,
    val bankName: String,
    var status: String,

) : BaseEntity()