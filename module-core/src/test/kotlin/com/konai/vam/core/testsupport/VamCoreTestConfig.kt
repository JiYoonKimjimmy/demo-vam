package com.konai.vam.core.testsupport

import com.konai.vam.core.config.CustomAuditorAware
import com.konai.vam.core.config.DataSourceConfig
import com.linecorp.kotlinjdsl.support.spring.data.jpa.autoconfigure.KotlinJdslAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Import(value = [
    DataSourceConfig::class,
    KotlinJdslAutoConfiguration::class
])
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@TestConfiguration
class VamCoreTestConfig {

    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return CustomAuditorAware()
    }

}