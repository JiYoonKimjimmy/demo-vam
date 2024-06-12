package com.konai.vam.core.restclient.koditn

data class KodItnGetMinimalInfoListRequest(
    val serviceIds: List<String>
) {
    val url = "/api/kod/v2/products/getMinimalInfoList"
}

data class KodItnProduct(
    val productId: String
)