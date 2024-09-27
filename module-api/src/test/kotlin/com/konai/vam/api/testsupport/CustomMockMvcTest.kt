package com.konai.vam.api.testsupport

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
annotation class CustomMockMvcTest