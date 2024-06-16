package com.konai.vam.core.common.restclient

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import com.konai.vam.core.common.error.exception.RestClientServiceException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import java.lang.NullPointerException
import java.util.*

@Component
abstract class BaseRestClient {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var externalUrlProperties: Properties

    @Autowired
    private lateinit var restClient: RestClient

    protected abstract val baseUrl: String

    protected fun generateBaseUrl(componentName: ComponentName): String {
        val propertyName = componentName.getPropertyName()
        return externalUrlProperties["$propertyName.url"]
            .takeIf { it != null }
            ?.let { "http://${it}" }
            ?: throw InternalServiceException(ErrorCode.EXTERNAL_URL_PROPERTY_NOT_DEFINED)
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

    private fun throwException(e: Exception): Nothing {
        logger.error(e.stackTraceToString())
        throw when(e) {
            is HttpClientErrorException -> RestClientServiceException(ErrorCode.EXTERNAL_SERVICE_ERROR, e.message)
            is NullPointerException -> RestClientServiceException(ErrorCode.EXTERNAL_SERVICE_RESPONSE_IS_NULL)
            else -> RestClientServiceException(ErrorCode.UNKNOWN_ERROR, e.message)
        }
    }

}