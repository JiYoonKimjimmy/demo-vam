package com.konai.vam.api.v1.sequencegenerator.service

import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

class SequenceGeneratorServiceTest : StringSpec({
    // logger
    val logger = LoggerFactory.getLogger(this::class.java)

    "일반 반복분 테스트" {
        val measureTimeMillis = measureTimeMillis {
            val size = 10
            repeat(size) {
                logger.info("$it")
                delay(1000)
            }
        }
        logger.info("measureTimeMillis : ${measureTimeMillis}ms")
    }

    "코루틴 반복분 테스트" {
        val measureTimeMillis = measureTimeMillis {
            runBlocking {
                val size = 10
                repeat(size) {
                    launch(Dispatchers.IO) {
                        logger.info("$it")
                        delay(1000)
                    }
                }
            }
        }
        logger.info("coroutine - measureTimeMillis : ${measureTimeMillis}ms")
    }

})