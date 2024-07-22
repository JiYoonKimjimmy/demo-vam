package com.konai.vam.core.repository.virtualaccountbank

import com.konai.vam.core.common.model.BasePageable
import com.konai.vam.core.repository.virtualaccountbank.entity.VirtualAccountBankEntity
import com.konai.vam.core.repository.virtualaccountbank.jdsl.VirtualAccountBankPredicate

interface VirtualAccountBankEntityAdapter {

    fun save(entity: VirtualAccountBankEntity): VirtualAccountBankEntity

    fun findByBankCode(bankCode: String): VirtualAccountBankEntity

    fun findAllByPredicate(predicate: VirtualAccountBankPredicate): BasePageable<VirtualAccountBankEntity?>

}