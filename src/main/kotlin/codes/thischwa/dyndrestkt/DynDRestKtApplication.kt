package codes.thischwa.dyndrestkt

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication

class DynDRestKtApplication

private val log = KotlinLogging.logger {}

fun main(args: Array<String>) {
    log.info { "Processing start of DynDRest" }
    runApplication<DynDRestKtApplication>(*args)
}
