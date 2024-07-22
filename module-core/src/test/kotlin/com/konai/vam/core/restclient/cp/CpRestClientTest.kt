package com.konai.vam.core.restclient.cp

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.RestClientServiceException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CpRestClientTest {

    @Autowired
    private lateinit var cpRestClient: CpRestClient

    @Test
    fun `CP 모바일 카드 정보 조회 요청하였지만, 삭제된 카드로 실패 응답 확인한다`() {
    	// given
        val token = "9491339602096694"
    	val request = CpGetCardsTokenRequest(token)

    	// when
        val exception = assertThrows<RestClientServiceException> { cpRestClient.getCardsToken(request) }

        // then
        println(exception.errorCode.message)
        assertThat(exception.errorCode).isEqualTo(ErrorCode.EXTERNAL_SERVICE_ERROR)
    }

}