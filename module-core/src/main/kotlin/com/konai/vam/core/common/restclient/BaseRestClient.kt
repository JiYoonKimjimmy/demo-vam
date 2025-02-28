package com.konai.vam.core.common.restclient

import com.konai.vam.core.common.error
import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.RestClientServiceException
import com.konai.vam.core.config.ExternalUrlProperties
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient

abstract class BaseRestClient {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var externalUrlProperties: ExternalUrlProperties

    @Autowired
    private lateinit var restClient: RestClient

    protected abstract val baseUrl: String

    protected fun generateBaseUrl(componentName: ComponentName): String {
        return externalUrlProperties.getProperty(componentName).url
    }

    protected fun <R> post(url: String, body: Any, response: Class<R>): R {
        return try {
            restClient.post().uri(url).body(body).retrieve().toEntity(response).body!!
        } catch (e: Exception) {
            throwException(e)
        }
    }

    protected fun <R> get(url: String, response: Class<R>): R {
        return try {
            restClient.get().uri(url).retrieve().toEntity(response).body!!
        } catch (e: Exception) {
            throwException(e)
        }
    }

    private fun throwException(exception: Exception): Nothing {
        logger.error(exception)
        throw when(exception) {
            is RestClientServiceException -> exception
            is HttpClientErrorException -> RestClientServiceException(ErrorCode.EXTERNAL_SERVICE_ERROR, exception.message)
            is NullPointerException -> RestClientServiceException(ErrorCode.EXTERNAL_SERVICE_RESPONSE_IS_NULL)
            else -> RestClientServiceException(ErrorCode.EXTERNAL_SERVICE_ERROR, exception.message)
        }
    }

}