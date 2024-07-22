package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.restclient.cardse.CardSeGetCardsInfoBatchIdRequest
import com.konai.vam.core.restclient.cardse.CardSeRestClient
import org.springframework.stereotype.Service

@Service
class VirtualAccountCardFetchService(
    private val cardSeRestClient: CardSeRestClient
): VirtualAccountCardFetchAdapter {

    override fun fetchParsByBatchId(batchId: String): List<String> {
        return cardSeRestClient.getCardsInfoBatchId(CardSeGetCardsInfoBatchIdRequest(batchId)).cardSeInfoList
            ?.takeIf { it.isNotEmpty() }
            ?.map { it.par }
            ?: throw InternalServiceException(ErrorCode.BATCH_ID_INVALID)
    }

}