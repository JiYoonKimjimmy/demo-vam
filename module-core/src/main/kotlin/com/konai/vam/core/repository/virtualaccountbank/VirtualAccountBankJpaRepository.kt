package com.konai.vam.core.repository.virtualaccountbank

import com.konai.vam.core.repository.virtualaccountbank.entity.VirtualAccountBankEntity
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface VirtualAccountBankJpaRepository : JpaRepository<VirtualAccountBankEntity, String>, KotlinJdslJpqlExecutor {

    fun findByBankCode(bankCode: String): Optional<VirtualAccountBankEntity>

}