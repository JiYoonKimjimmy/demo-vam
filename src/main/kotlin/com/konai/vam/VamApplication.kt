package com.konai.vam

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VamApplication

fun main(args: Array<String>) {
    runApplication<VamApplication>(*args)
}
