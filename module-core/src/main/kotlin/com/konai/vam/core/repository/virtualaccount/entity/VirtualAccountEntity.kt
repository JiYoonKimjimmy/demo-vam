package com.konai.vam.core.repository.virtualaccount.entity

import com.konai.vam.core.common.converter.EncryptionCardInfoConverter
import com.konai.vam.core.common.entity.BaseEntity
import com.konai.vam.core.enumerate.VirtualAccountCardConnectStatus
import com.konai.vam.core.enumerate.VirtualAccountConnectType
import com.konai.vam.core.enumerate.VirtualAccountStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "VIRTUAL_ACCOUNTS")
class VirtualAccountEntity(

    @Id
    @GeneratedValue
    val id: Long? = null,
    @Convert(converter = EncryptionCardInfoConverter::class)
    val accountNo: String,
    val bankCode: String,
    @Enumerated(EnumType.STRING)
    val connectType: VirtualAccountConnectType,
    @Enumerated(EnumType.STRING)
    var status: VirtualAccountStatus,
    val par: String? = null,
    @Enumerated(EnumType.STRING)
    val cardConnectStatus: VirtualAccountCardConnectStatus? = null,
    val cardConnected: LocalDateTime? = null,
    val cardDisconnected: LocalDateTime? = null,
    val cardSeBatchId: String? = null,

) : BaseEntity()