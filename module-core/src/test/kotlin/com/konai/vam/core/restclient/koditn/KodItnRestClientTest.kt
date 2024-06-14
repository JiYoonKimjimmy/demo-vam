package com.konai.vam.core.restclient.koditn

import com.konai.vam.core.config.RestClientConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(RestClientConfig::class)
@SpringBootTest
class KodItnRestClientTest {

    @Autowired
    private lateinit var kodItnRestClient: KodItnRestClient

    @Test
    fun `KOD_ITN 상품 정보 조회 요청하여 정상 응답 확인한다`() {
    	// given
    	val productId = "953365002513000"
        val request = KodItnGetProductsBasicInfoRequest(productId)

    	// when
        val response = kodItnRestClient.getProductsBasicInfo(request)

        // then
        assertThat(response).isNotNull
        assertThat(response.productId).isEqualTo(productId)
    }

}