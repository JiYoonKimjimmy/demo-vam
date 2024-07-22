package com.konai.vam.core.restclient.kms

data class KmsGetEncryptionKeyRequest(
    val requestData: String,
    val functionCallIdentifier: Int = 0,
    val validityPeriod: Int = 0,
    val apiKey: String = "f58ce930622d7b9b2c89072a34f1e9ca5482dfc6",
    val mechanism: String = "AES_ECB",
    val keyLabel: String = "2410400906EF00000083.SECRET.AES.TRANS.01.01",
    val padding: String = "NOPADDING",
) {
    val url by lazy { "/api/crypto/encrypt" }
}

data class KmsGetEncryptionKeyResponse(
    val functionCallIdentifier: Int,
    val executionStatus: ExecutionStatus,
    val validityPeriod: Int,
    val processingStart: Long,
    val processingEnd: Long,
    val apiKey: String,
    val responseData: String,
) {
    data class ExecutionStatus(
        val statusCodeData: Map<String, Any>,
        val statusType: String,
    )
}
