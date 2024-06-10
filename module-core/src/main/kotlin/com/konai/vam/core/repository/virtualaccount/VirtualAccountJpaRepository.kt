package com.konai.vam.core.repository.virtualaccount

import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository

interface VirtualAccountJpaRepository : JpaRepository<VirtualAccountEntity, Long>, KotlinJdslJpqlExecutor