package com.konai.vam.api.v1.batch.service

import com.konai.vam.api.v1.kotestspec.KoTestBehaviorSpec
import com.konai.vam.core.common.model.VoidResponse
import io.mockk.every
import io.mockk.verify

class VirtualAccountCardBatchServiceTest : KoTestBehaviorSpec({

    val virtualAccountCardBatchService = virtualAccountCardBatchService()
    val mockVamBatchRestclient = mockVamBatchRestclient()

    given("가상 계좌 카드 BULK 연결 Batch API 요청하여") {
        val batchId = "batchId"
        val serviceId = "serviceId"

        `when`("`module-batch` API 요청 성공하는 경우") {

            every { mockVamBatchRestclient.virtualAccountBulkCardConnect(any()) } returns VoidResponse()

            virtualAccountCardBatchService.virtualAccountBulkCardConnect(batchId, serviceId)

            then("성공 응답 확인한다") {
                verify(exactly = 1) { mockVamBatchRestclient.virtualAccountBulkCardConnect(any()) }
            }
        }
    }

})