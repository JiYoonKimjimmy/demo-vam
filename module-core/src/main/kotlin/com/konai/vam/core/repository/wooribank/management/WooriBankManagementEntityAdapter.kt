package com.konai.vam.core.repository.wooribank.management

import com.konai.vam.core.repository.wooribank.management.entity.WooriBankManagementEntity
import com.konai.vam.core.repository.wooribank.management.jdsl.WooriBankManagementPredicate
import java.util.*

interface WooriBankManagementEntityAdapter {

    fun save(entity: WooriBankManagementEntity): WooriBankManagementEntity

    fun findByPredicate(predicate: WooriBankManagementPredicate): Optional<WooriBankManagementEntity>

}