package com.konai.vam.core.repository.virtualaccountbank

class VirtualAccountBankConst(
    val bankCode: String,
    val bankName: String,
    val bankEngName: String,
) {

    companion object {
        val woori: VirtualAccountBankConst = VirtualAccountBankConst("020", "우리은행", "Woori Bank")
    }

}