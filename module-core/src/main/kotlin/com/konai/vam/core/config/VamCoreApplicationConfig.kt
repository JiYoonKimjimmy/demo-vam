package com.konai.vam.core.config

import com.konasl.commonlib.logging.filter.EnableLoggingFilter
import com.konasl.commonlib.logging.helper.masking.MaskingProperties
import com.konasl.commonlib.springweb.correlation.EnableCorrelationFilter
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*

@EnableLoggingFilter
@EnableCorrelationFilter
@EnableConfigurationProperties(MaskingProperties::class)
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