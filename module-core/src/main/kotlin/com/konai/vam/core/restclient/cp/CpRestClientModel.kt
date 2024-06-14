package com.konai.vam.core.restclient.cp

data class CpGetCardsTokenRequest(
    val token: String,
) {
    val url by lazy { "/api/cards/token?token=$token" }
}

data class CpGetCardsTokenResponse(
    val cardId: String? = null,
    val par: String? = null,
    val token: String? = null,
    val expireDate: String? = null,
    val mpaId: String? = null,
    val userId: String? = null,
    val serviceId: String? = null,
    val status: String? = null,
)