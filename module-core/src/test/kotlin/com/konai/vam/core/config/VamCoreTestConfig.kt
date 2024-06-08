package com.konai.vam.core.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@TestConfiguration
class VamCoreTestConfig {

    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return CustomAuditorAware()
    }

}