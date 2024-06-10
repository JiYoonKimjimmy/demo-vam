package com.konai.vam.core.repository.virtualaccount

import com.konai.vam.core.repository.virtualaccount.entity.VirtualAccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface VirtualAccountJpaRepository : JpaRepository<VirtualAccountEntity, Long>