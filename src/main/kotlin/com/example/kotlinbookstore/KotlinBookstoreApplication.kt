package com.example.kotlinbookstore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinBookstoreApplication

fun main(args: Array<String>) {
    runApplication<KotlinBookstoreApplication>(*args)
}
