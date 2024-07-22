package com.konai.vam.api.v1.wooribank.service.management

import com.konai.vam.api.v1.wooribank.service.management.domain.WooriBankManagement
import com.konai.vam.core.repository.wooribank.management.jdsl.WooriBankManagementPredicate

interface WooriBankManagementFindAdapter {

    fun findByPredicate(predicate: WooriBankManagementPredicate): WooriBankManagement?

}