package com.konai.vam.core.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.transaction.annotation.EnableTransactionManagement

//@EnableLoggingFilter
//@EnableCorrelationFilter
//@EnableConfigurationProperties(MaskingProperties::class)
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Configuration
class VamCoreApplicationConfig {

    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return CustomAuditorAware()
    }

}