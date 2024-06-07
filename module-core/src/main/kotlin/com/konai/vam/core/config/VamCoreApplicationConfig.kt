package com.konai.vam.core.config

import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

//@EnableLoggingFilter
//@EnableCorrelationFilter
//@EnableConfigurationProperties(MaskingProperties::class)
@EnableTransactionManagement
@Configuration
class VamCoreApplicationConfig