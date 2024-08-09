package com.konai.vam.core.restclient.cardse

data class CardSeGetCardsInfoBatchIdRequest(
    val batchId: String,
) {
    val url by lazy { "/cards/info/batchId/$batchId" }
}

data class CardSeGetCardsInfoBatchIdResponse(
    val cardSeInfoList: List<CardSeInfo>? = emptyList(),
    val auditRegisteredAt: Long? = null,
    val first: Boolean? = null,
    val last: Boolean? = null,
    val number: Int? = null,
    val numberOfElements: Int? = null,
    val size: Int? = null,
    val totalElements: Int? = null,
    val totalPages: Int? = null,
    val sort: String? = null,
    val reason: String? = null,
    val message: String? = null,
) {
    data class CardSeInfo(
        val par: String,
        val token: String,
    )

    fun getPars(): List<String>? {
        return cardSeInfoList?.takeIf { it.isNotEmpty() }?.map { it.par }
    }

}

data class CardSeGetCardsInfoTokenRequest(
    val token: String,
) {
    val url by lazy { "/cards/info/token/$token" }
}

data class CardSeGetCardsInfoTokenResponse(
    val aspList: List<String>? = emptyList(),
    val connectionType: String? = null,
    val expiryDate: Int? = null,
    val hceCardPar: String? = null,
    val originalPar: String? = null,
    val serviceId: String? = null,
    val status: String? = null,
    val token: String? = null
)