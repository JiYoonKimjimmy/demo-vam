package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

import com.konai.vam.batch.v1.virtualaccount.batchfile.service.VirtualAccountBachFileCreateAdapter
import com.konai.vam.batch.v1.virtualaccount.batchhistory.service.domain.VirtualAccountBatchHistory
import com.konai.vam.batch.v1.virtualaccount.cardmanagement.service.domain.VirtualAccountCardConnect
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.restclient.cardse.CardSeGetCardsInfoBatchIdRequest
import com.konai.vam.core.restclient.cardse.CardSeRestClient
import com.konai.vam.core.restclient.koditn.KodItnGetProductsBasicInfoRequest
import com.konai.vam.core.restclient.koditn.KodItnRestClient
import org.springframework.stereotype.Service

@Service
class VirtualAccountCardManagementService(

    private val virtualAccountBachFileCreateAdapter: VirtualAccountBachFileCreateAdapter,
    private val virtualAccountCardConnectAdapter: VirtualAccountCardConnectAdapter,
    private val kodItnRestClient: KodItnRestClient,
    private val cardSeRestClient: CardSeRestClient

) : VirtualAccountCardManagementAdapter {

    override fun connectBulkCard(batchId: String, serviceId: String) {
        val history = connectBulkCardToVirtualAccount(batchId, serviceId)
        virtualAccountBachFileCreateAdapter.createBatchFile(batchId, history)
    }

    private fun connectBulkCardToVirtualAccount(batchId: String, serviceId: String): VirtualAccountBatchHistory {
        val domain = VirtualAccountCardConnect(
            batchId = batchId,
            serviceId = serviceId,
            bankCode = fetchBankCode(serviceId),
            pars = fetchPars(batchId)
        )
        return connectCardToVirtualAccounts(domain)
    }

    private fun fetchBankCode(serviceId: String): String {
        return kodItnRestClient.getProductsBasicInfo(KodItnGetProductsBasicInfoRequest(serviceId))
            .checkFixableVirtualAccountPolicy()
            .virtualAccountBankCode!!
    }

    private fun fetchPars(batchId: String): List<String> {
        return cardSeRestClient.getCardsInfoBatchId(CardSeGetCardsInfoBatchIdRequest(batchId))
            .getPars()
            ?: throw InternalServiceException(ErrorCode.BATCH_ID_INVALID)
    }

    private fun connectCardToVirtualAccounts(domain: VirtualAccountCardConnect): VirtualAccountBatchHistory {
        return virtualAccountCardConnectAdapter.connectCardToVirtualAccounts(domain).batchHistory
    }

}