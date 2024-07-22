package com.konai.vam.api.v1.virtualaccountbank.service

import com.konai.vam.api.v1.virtualaccountbank.service.domain.VirtualAccountBank
import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.repository.virtualaccountbank.jdsl.VirtualAccountBankPredicate

interface VirtualAccountBankFindAdapter {

    fun findByBankCode(bankCode: String): VirtualAccountBank

    fun findAllByPredicate(predicate: VirtualAccountBankPredicate): BasePageable<VirtualAccountBank>

}