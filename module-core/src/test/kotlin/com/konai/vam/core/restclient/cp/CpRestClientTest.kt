package com.konai.vam.core.restclient.cp

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CpRestClientTest {

    @Autowired
    private lateinit var cpRestClient: CpRestClient

    @Test
    fun `CP 모바일 카드 정보 조회 요청하여 정상 응답 성공한다`() {
    	// given
        val token = "9491339602096694"
    	val request = CpGetCardsTokenRequest(token)

    	// when
    	val response = cpRestClient.getCardsToken(request)

    	// then
        assertThat(response).isNotNull
        assertThat(response?.token).isEqualTo(token)
    }

}