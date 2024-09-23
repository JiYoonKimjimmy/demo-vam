package com.konai.vam.core.repository.parentaccount

import com.konai.vam.core.repository.parentaccount.entity.ParentAccountEntity
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository

interface ParentAccountJpaRepository : JpaRepository<ParentAccountEntity, Long>, KotlinJdslJpqlExecutor {

    fun findByParentAccountNo(parentAccountNo: String): ParentAccountEntity?

    fun existsByParentAccountNoAndBankCode(parentAccountNo: String, bankCode: String): Boolean

}