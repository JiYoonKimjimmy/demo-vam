package com.konai.vam.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VamCoreApplication

fun main(args: Array<String>) {
    runApplication<VamCoreApplication>(*args)
}