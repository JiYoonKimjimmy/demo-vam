package com.konai.vam.core.repository.wooribank.management

import com.konai.vam.core.enumerate.WooriBankResponseCode
import com.konai.vam.core.repository.wooribank.management.entity.WooriBankManagementEntity
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WooriBankManagementJpaRepository : JpaRepository<WooriBankManagementEntity, Long>, KotlinJdslJpqlExecutor {

    fun findByMessageNoAndResponseCode(messageNo: String, responseCode: WooriBankResponseCode): Optional<WooriBankManagementEntity>

}