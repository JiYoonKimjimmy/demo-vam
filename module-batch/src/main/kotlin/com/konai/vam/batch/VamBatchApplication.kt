package com.konai.vam.batch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VamBatchApplication

fun main(args: Array<String>) {
    runApplication<VamBatchApplication>(*args)
}