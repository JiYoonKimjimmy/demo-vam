package com.konai.vam.core.repository.virtualaccount.entity

import com.konai.vam.core.common.converter.DatabaseCustomerInfoColumnConverter
import com.konai.vam.core.common.entity.BaseEntity
import jakarta.persistence.*

@Entity(name = "VIRTUAL_ACCOUNT")
class VirtualAccountEntity(

    @Id
    @GeneratedValue
    val id: Long? = null,
    @Convert(converter = DatabaseCustomerInfoColumnConverter::class)
    val accountNumber: String,
    val bankCode: String,
    val bankName: String,
    var status: String,

) : BaseEntity()