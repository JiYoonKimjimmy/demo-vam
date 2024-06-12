package com.konai.vam.core.config

import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig : RestClientAutoConfiguration() {

    @Bean
    fun restClient(): RestClient {
        return RestClient
            .builder()
            .build()
    }

}