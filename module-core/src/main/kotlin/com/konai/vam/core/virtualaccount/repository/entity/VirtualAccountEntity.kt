package com.konai.vam.core.virtualaccount.repository.entity

import com.konai.vam.core.common.converter.DatabaseCustomerInfoColumnConverter
import com.konai.vam.core.common.entity.BaseEntity
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

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