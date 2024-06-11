package com.konai.vam.core.repository.virtualaccount.entity

import com.konai.vam.core.common.converter.DatabaseCustomerInfoColumnConverter
import com.konai.vam.core.common.entity.BaseEntity
import com.konai.vam.core.enumerate.VirtualAccountMappingType
import com.konai.vam.core.enumerate.VirtualAccountStatus
import com.konai.vam.core.enumerate.YesOrNo
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
    @Enumerated(EnumType.STRING)
    val mappingType: VirtualAccountMappingType,
    @Enumerated(EnumType.STRING)
    val mappingYn: YesOrNo,
    @Enumerated(EnumType.STRING)
    var status: VirtualAccountStatus,

) : BaseEntity()