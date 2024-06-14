package com.konai.vam.core.repository.virtualaccount.entity

import com.konai.vam.core.common.converter.DatabaseCustomerInfoColumnConverter
import com.konai.vam.core.common.entity.BaseEntity
import com.konai.vam.core.enumerate.VirtualAccountConnectType
import com.konai.vam.core.enumerate.VirtualAccountStatus
import jakarta.persistence.*

@Entity(name = "VIRTUAL_ACCOUNTS")
class VirtualAccountEntity(

    @Id
    @GeneratedValue
    val id: Long? = null,
    @Convert(converter = DatabaseCustomerInfoColumnConverter::class)
    val accountNo: String,
    val bankCode: String,
    @Enumerated(EnumType.STRING)
    val connectType: VirtualAccountConnectType,
    @Enumerated(EnumType.STRING)
    var status: VirtualAccountStatus,

) : BaseEntity()