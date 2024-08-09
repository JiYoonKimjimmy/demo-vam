package com.konai.vam.core.restclient.cs

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestClient

@SpringBootTest
class CsRestClientTest {

    @SpykBean
    private lateinit var csRestClient: CsRestClient

    @MockkBean
    private lateinit var restClient: RestClient

    private val baseUrl = "http://118.33.122.28:15820/cs"

    @Test
    fun `CS 시스템 어드인 충전 요청하여 정상 응답 성공한다`() {
    	// given
        val par = "par"
        val amount = "1000"
        val serviceId = "serviceId"
        val rechargerId = "rechargerId"
        val isPushRequired = false
        val request = CsPostRechargesSystemManualsRequest(par, amount, serviceId, rechargerId, isPushRequired)
        val response = CsPostRechargesSystemManualsResponse(par = par, amount = amount, transactionId = "transactionId", nrNumber = "nrNumber", reason = "000")

        every { restClient.post().uri("$baseUrl${request.url}").body(request).retrieve().toEntity(CsPostRechargesSystemManualsResponse::class.java).body!! } returns response

    	// when
    	val result = csRestClient.postRechargesSystemManuals(request)

    	// then
        assertThat(result).isNotNull
        assertThat(result.reason).isEqualTo("000")
        assertThat(result.par).isEqualTo(par)
        assertThat(result.amount).isEqualTo(amount)
    }

    @Test
    fun `CS 시스템 어드인 충전 취소 요청하여 정상 응답 성공한다`() {
    	// given
        val transactionId = "transactionId"
        val par = "par"
        val amount = "1000"
        val request = CsPostRechargesSystemManualsReversalRequest(transactionId = transactionId, par = par, amount = amount)
        val response = CsPostRechargesSystemManualsReversalResponse(transactionId = transactionId, reason = "000")

        every { restClient.post().uri("$baseUrl${request.url}").body(request).retrieve().toEntity(CsPostRechargesSystemManualsReversalResponse::class.java).body!! } returns response

    	// when
    	val result = csRestClient.postRechargesSystemManualsReversal(request)

    	// then
        assertThat(result).isNotNull
        assertThat(result.reason).isEqualTo("000")
        assertThat(result.transactionId).isEqualTo(transactionId)
    }

}