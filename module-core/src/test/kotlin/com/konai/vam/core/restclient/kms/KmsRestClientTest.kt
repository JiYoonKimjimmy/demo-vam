package com.konai.vam.core.restclient.kms

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KmsRestClientTest {

    @Autowired
    private lateinit var kmsRestClient: KmsRestClient

    @Test
    fun `KMS AES 대칭키 발급을 요청하여 키를 발급받는다`(){
        // given
        val batchId = "3935333336353030323532393030303233303230363232353130393136493030"
        val request = KmsGetEncryptionKeyRequest(requestData = batchId)

        // when
        val response = kmsRestClient.getEncryptionKey(request)

        // then
        assertThat(response).isNotNull()
        assertThat(response.responseData).isNotNull()
    }
}