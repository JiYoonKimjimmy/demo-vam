package com.konai.vam.batch.v1.virtualaccount.cardmanagement.service

import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.restclient.cardse.CardSeGetCardsInfoBatchIdResponse
import com.konai.vam.core.restclient.cardse.CardSeGetCardsInfoBatchIdResponse.CardSeInfo
import com.konai.vam.core.restclient.cardse.CardSeRestClient
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class VirtualAccountCardFetchServiceTest : BehaviorSpec({

    given("CARDSE에 batchId에 해당하는 par의 리스트를 조회했을 때") {

        val mockRestClient = mockk<CardSeRestClient>()
        val cardSeClientService = VirtualAccountCardFetchService(mockRestClient)
        val batchId = "00000800029500017071809205815I00001"

        `when`("해당하는 Par 가 없을 때") {

            every { mockRestClient.getCardsInfoBatchId(any()) } returns CardSeGetCardsInfoBatchIdResponse(emptyList())

            then("ErrorCode 가 004인 InternalServiceException 를 발생시킨다.") {
                val exception = shouldThrow<InternalServiceException> {
                    cardSeClientService.fetchParsByBatchId(batchId)
                }

                exception.errorCode.code shouldBe "004"
                exception.errorCode.message shouldBe "This batchId is invalid"
            }
        }

        `when`("해당하는 Par가 있을 때") {
            every { mockRestClient.getCardsInfoBatchId(any()) } returns CardSeGetCardsInfoBatchIdResponse(
                cardSeInfoList = listOf(
                    CardSeInfo(par = "Q12EA85B4FFF21783695C1C98EA", token = "9810010000319483"),
                    CardSeInfo(par = "Q12EA85B4FFF21783695C1C98EB", token = "9810010000319482")
                )
            )

            then("사이즈가 1 이상인 리스트를 반환한다.") {
                val fetchedParList = cardSeClientService.fetchParsByBatchId(batchId)
                fetchedParList.size shouldBeGreaterThanOrEqual 1
            }
        }
    }
})