package com.konai.vam.core.restclient.cardse

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CardSeRestClientTest {

    @Autowired
    private lateinit var cardSeRestClient: CardSeRestClient

    @Test
    fun `CARD_SE 실물 카드 발급 목록 조회 요청하여 정상 응답 성공한다`() {
    	// given
    	val batchId = "00018700000400024061317475501I00001"
        val request = CardSeGetCardsInfoBatchIdRequest(batchId)

    	// when
        val response = cardSeRestClient.getCardsInfoBatchId(request)

        // then
        assertThat(response).isNotNull
        assertThat(response.cardSeInfoList).isNotEmpty
        assertThat(response.cardSeInfoList).hasSizeGreaterThan(0)
    }

    @Test
    fun `CARD_SE 실물 카드 번호 기준 정보 조회 요청하여 정상 응답 성공한다`() {
    	// given
        val token = "5366528102612378"
    	val request = CardSeGetCardsInfoTokenRequest(token)

    	// when
        val response = cardSeRestClient.getCardsInfoToken(request)

        // then
        assertThat(response).isNotNull
        assertThat(response.token).isEqualTo(token)
    }

}