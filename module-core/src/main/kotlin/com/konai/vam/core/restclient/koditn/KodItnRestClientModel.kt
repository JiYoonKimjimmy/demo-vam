package com.konai.vam.core.restclient.koditn

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException

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
) {

    fun checkFixableVirtualAccountPolicy(): KodItnProduct {
        if (this.virtualAccountMappingType != "01" || this.virtualAccountBankCode == null) {
            // 가상 계좌 연결 구분 '고정' 아니거나, 은행코드 정보가 없는 경우, 예외 발생
            throw InternalServiceException(ErrorCode.SERVICE_ID_IS_INVALID)
        }
        return this
    }

}