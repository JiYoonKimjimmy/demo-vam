package com.konai.vam.core.common.restclient

import com.konai.vam.core.common.error.ErrorCode
import com.konai.vam.core.common.error.exception.InternalServiceException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.lang.NullPointerException
import java.util.*

@Component
abstract class BaseRestClient {

    @Autowired
    lateinit var externalUrlProperties: Properties

    @Autowired
    lateinit var restClient: RestClient

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
        throw when(e) {
            is NullPointerException -> InternalServiceException(ErrorCode.EXTERNAL_SERVICE_RESPONSE_IS_NULL)
            else -> InternalServiceException(ErrorCode.EXTERNAL_SERVICE_ERROR)
        }
    }

}