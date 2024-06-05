package com.konai.vam.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VamApiApplication

fun main(args: Array<String>) {
    runApplication<VamApiApplication>(*args)
}
