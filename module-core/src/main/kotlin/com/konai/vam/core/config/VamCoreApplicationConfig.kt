package com.konai.vam.core.config

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*

//@EnableLoggingFilter
//@EnableCorrelationFilter
//@EnableConfigurationProperties(MaskingProperties::class)
@EnableTransactionManagement
@Configuration
class VamCoreApplicationConfig {

    @Bean
    fun externalUrlProperties(): Properties {
        return YamlPropertiesFactoryBean()
            .also { it.setResources(ClassPathResource("application-external-url.yml")) }
            .`object`!!
    }

}