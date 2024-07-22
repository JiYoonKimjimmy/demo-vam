package fixtures

import com.konai.vam.core.restclient.koditn.KodItnProduct

class KodItnRestClientModelFixture {

    fun make(productId: String, mappingType: String, virtualAccountBankCode: String? = null): KodItnProduct {
        return KodItnProduct(
            productId = productId,
            virtualAccountBankCode = virtualAccountBankCode,
            virtualAccountMappingType = mappingType,
            virtualAccountRechargeWaitAmountYn = null,
            virtualAccountRechargeMaxWaitAmount = null
        )
    }

}