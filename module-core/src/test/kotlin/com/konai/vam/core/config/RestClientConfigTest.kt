package com.konai.vam.core.config

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.web.client.RestClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Import(value = [RestClientConfig::class])
@SpringBootTest
class RestClientConfigTest(
    @Autowired private val restClient: RestClient
) {

    @Test
    fun `RestClient POST 요청 성공한다`() {
        // given
        val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
        val correlationId = "$now-VAM-TEST"
        val aspId = "953365000000000"
        val userId = "3100000880"
        val body = mapOf("userId" to 3100000880)

        //MDC.put(CORRELATION_ID_LOG_FIELD.getName(), correlationId)
        //MDC.put(ASP_ID_LOG_FIELD.getName(), aspId)
        //MDC.put(USER_ID_LOG_FIELD.getName(), userId)

        // when then
        restClient
            .post()
            .uri("http://118.33.122.32:10010/mobile-platform-1.0/api/v3/user")
            .body(body)
            .exchange { _, response ->
                assertFalse(response.statusCode.isError)
                //assertEquals(response.headers[CORRELATION_ID_HEADER_FIELD.getName()]?.first(), correlationId)
                //assertEquals(response.headers[ASP_ID_HEADER_FIELD.getName()]?.first(), aspId)
                //assertEquals(response.headers[USER_ID_HEADER_FIELD.getName()]?.first(), userId)
            }
    }

}