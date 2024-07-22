package com.konai.vam.core.common.restclient

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.config.VamCoreApplicationConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class BaseRestClientTest {

    private lateinit var externalUrlProperties: Properties

    @BeforeEach
    fun before() {
        externalUrlProperties = VamCoreApplicationConfig().externalUrlProperties()
    }

    private fun generateBaseUrl(componentName: String): String {
        return externalUrlProperties["$componentName.url"].takeIf { it != null }?.let { "http://$it" }
            ?: throw InternalServiceException(ErrorCode.EXTERNAL_URL_PROPERTY_NOT_DEFINED)
    }

    @Test
    fun `FEP client base url 정보 생성 성공한다`() {
    	// given
        val componentName = ComponentName.FEP.getPropertyName()

        // when
        val result = generateBaseUrl(componentName)

        // then
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("http://10.30.210.154:30005")
    }

    @Test
    fun `CARD_SE client base url 정보 생성 성공한다`() {
    	// given
        val componentName = ComponentName.CARD_SE.getPropertyName()

        // when
        val result = generateBaseUrl(componentName)

        // then
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("http://10.30.210.153:10300/cardse-service/api/cardse")
    }

    @Test
    fun `URL 정의되지 않은 Component base url 정보 생성하는 경우 실패한다`() {
    	// given
        val componentName = "map"

    	// when
    	val exception = assertThrows<InternalServiceException> { generateBaseUrl(componentName) }

    	// then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.EXTERNAL_URL_PROPERTY_NOT_DEFINED)
    }

}