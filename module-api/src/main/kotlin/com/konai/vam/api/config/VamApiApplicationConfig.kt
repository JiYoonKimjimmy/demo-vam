package com.konai.vam.api.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@EntityScan(basePackages = ["com.konai.vam.core"])
@ComponentScan(basePackages = ["com.konai.vam.core"])
@Configuration
class VamApiApplicationConfig