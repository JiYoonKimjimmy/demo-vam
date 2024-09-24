package com.konai.vam.api.v1.parentaccount.service.domain

data class ParentAccount(
    val id: Long? = null,
    val parentAccountNo: String,
    val bankCode: String
) {

    fun updateParentAccountNoOrBankCode(parentAccountNo: String?, bankCode: String?): ParentAccount {
        return this.copy(
            parentAccountNo = parentAccountNo?.takeIf { it.isNotEmpty() } ?: this.parentAccountNo,
            bankCode = bankCode?.takeIf { it.isNotEmpty() } ?: this.bankCode
        )
    }

}