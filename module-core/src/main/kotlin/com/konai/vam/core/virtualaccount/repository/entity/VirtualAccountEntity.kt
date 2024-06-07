package com.konai.vam.core.virtualaccount.repository.entity

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
    val status: String,
)