package com.konai.vam.batch.config

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EntityScan(basePackages = ["com.konai.vam.core"])
@ComponentScan(basePackages = ["com.konai.vam.core", "com.konai.vam.api"])
@EnableJpaRepositories(basePackages = ["com.konai.vam.core"])
@EnableBatchProcessing(dataSourceRef = "getDataSource")
@Configuration
class VamBatchApplicationConfig