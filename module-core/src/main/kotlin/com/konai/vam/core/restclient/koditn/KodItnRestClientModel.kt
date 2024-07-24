package com.konai.vam.core.restclient.koditn

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.enumerate.VirtualAccountConnectType

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
        if (this.virtualAccountBankCode == null) throw InternalServiceException(ErrorCode.SERVICE_ID_IS_INVALID)
        if (this.virtualAccountMappingType != VirtualAccountConnectType.FIXATION.code) throw throw InternalServiceException(ErrorCode.SERVICE_POLICY_NOT_SUPPORT_FOR_FIXATION_TYPE)
        return this
    }

}