package com.konai.vam.core.config

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.RestClient

@Import(value = [RestClientConfig::class])
@SpringBootTest
class RestClientConfigTest(
    @Autowired private val restClient: RestClient
) {

    @Test
    fun `RestClient POST 요청 성공한다`() {
        // given
        val userId = "3100000880"
        val body = mapOf("userId" to userId)

        // when then
        restClient
            .post()
            .uri("http://118.33.122.32:10010/mobile-platform-1.0/api/v3/user")
            .body(body)
            .exchange { _, response ->
                assertFalse(response.statusCode.isError)
                assertTrue(response.statusCode.is2xxSuccessful)
                assertEquals(response.statusCode, HttpStatusCode.valueOf(200))
            }
    }

}