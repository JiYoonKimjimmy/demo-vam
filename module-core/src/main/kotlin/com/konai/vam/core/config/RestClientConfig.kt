package com.konai.vam.core.config

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Configuration
class RestClientConfig : RestClientAutoConfiguration() {
    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun restClient(): RestClient {
        return RestClient
            .builder()
            .requestInitializer {
                it.headers["X-KM-Correlation-Id"] = generateCorrelationId()
            }
            .build()
    }

    private fun generateCorrelationId(): String {
        val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))
        val uuid = UUID.randomUUID().toString().split("-")[0]
        val correlationId = "$now-VAM-$uuid"
        logger.info("Generate Correlation Id : $correlationId")
        return correlationId
    }

}