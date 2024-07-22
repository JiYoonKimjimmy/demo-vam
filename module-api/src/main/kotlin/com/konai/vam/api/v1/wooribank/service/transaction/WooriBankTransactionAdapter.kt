package com.konai.vam.api.v1.wooribank.service.transaction

import com.konai.vam.api.v1.wooribank.service.transaction.domain.WooriBankTransaction

interface WooriBankTransactionAdapter {

    fun deposit(domain: WooriBankTransaction): WooriBankTransaction

    fun depositCancel(domain: WooriBankTransaction): WooriBankTransaction

    fun depositConfirm(domain: WooriBankTransaction): WooriBankTransaction

}