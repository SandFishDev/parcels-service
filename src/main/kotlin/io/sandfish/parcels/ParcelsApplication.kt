package io.sandfish.parcels

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ParcelsApplication

fun main(args: Array<String>) {
    runApplication<ParcelsApplication>(*args)
}
