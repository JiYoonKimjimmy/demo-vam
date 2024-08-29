package com.konai.vam.core.config

import com.konasl.commonlib.logging.filter.EnableLoggingFilter
import com.konasl.commonlib.logging.helper.masking.MaskingProperties
import com.konasl.commonlib.springweb.correlation.EnableCorrelationFilter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableLoggingFilter
@EnableCorrelationFilter
@EnableTransactionManagement
@EnableConfigurationProperties(MaskingProperties::class)
@Configuration
class VamCoreApplicationConfig