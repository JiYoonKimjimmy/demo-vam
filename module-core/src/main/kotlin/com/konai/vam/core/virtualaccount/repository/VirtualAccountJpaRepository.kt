package com.konai.vam.core.virtualaccount.repository

import com.konai.vam.core.virtualaccount.repository.entity.VirtualAccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface VirtualAccountJpaRepository : JpaRepository<VirtualAccountEntity, Long>