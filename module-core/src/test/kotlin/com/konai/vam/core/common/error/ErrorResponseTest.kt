package com.konai.vam.core.common.error

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ErrorResponseTest {

    @Test
    fun `'detailMessage' 에러 메시지 변환하여 ErrorReponse 생성 성공한다`() {
    	// given
        val detailMessage = "500 : \"{\"result\":{\"status\":\"FAILED\",\"code\":\"218_9000_903\",\"message\":\"Virtual Account Batch Service. External API service error.\",\"detailMessage\":\"404 Not Found: \\\"{\\\"reason\\\":\\\"32_0001_008\\\",\\\"message\\\":\\\"CardSe info retrieval failed. No batch raw data found for given batch id. BatchId : string.\\\"}\\\"\"}}\""

        // when
        val response = ErrorResponse.toResponseEntity(detailMessage)

        // then
        assertThat(response.statusCode.value()).isEqualTo(500)
        assertThat(response.body?.result?.code).isEqualTo("218_9000_903")
    }

}