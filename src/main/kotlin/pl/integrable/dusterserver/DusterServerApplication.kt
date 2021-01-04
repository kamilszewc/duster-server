package pl.integrable.dusterserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DusterServerApplication

fun main(args: Array<String>) {
    runApplication<DusterServerApplication>(*args)
}
