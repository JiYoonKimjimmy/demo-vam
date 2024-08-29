package com.konai.vam.core.common.restclient

import com.konai.vam.core.config.ExternalUrlProperties
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BaseRestClientTest {

    private val externalUrlProperties = ExternalUrlProperties()

    private fun generateBaseUrl(componentName: ComponentName): String {
        return externalUrlProperties.getProperty(componentName).url
    }

    @Test
    fun `FEP client base url 정보 생성 성공한다`() {
    	// given
        val componentName = ComponentName.FEP

        // when
        val result = generateBaseUrl(componentName)

        // then
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("http://118.33.122.34:25002")
    }

    @Test
    fun `CARD_SE client base url 정보 생성 성공한다`() {
    	// given
        val componentName = ComponentName.CARD_SE

        // when
        val result = generateBaseUrl(componentName)

        // then
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("http://118.33.122.28:10300/cardse-service/api/cardse")
    }

}