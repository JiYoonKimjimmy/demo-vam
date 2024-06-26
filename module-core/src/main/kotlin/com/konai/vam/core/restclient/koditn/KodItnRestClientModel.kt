package com.konai.vam.core.restclient.koditn

data class KodItnGetProductsBasicInfoRequest(
    val productId: String
) {
    val url by lazy { "/api/kod/v2/products/$productId/basicInfo" }
}

data class KodItnProduct(
    val productId: String,
    val virtualAccountMappingType: String?,
    val virtualAccountBankCode: String?,
    val virtualAccountRechargeWaitAmountYn: String?,
    val virtualAccountRechargeMaxWaitAmount: Long?,
)