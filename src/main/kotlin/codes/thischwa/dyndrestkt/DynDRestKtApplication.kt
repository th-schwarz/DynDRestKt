package codes.thischwa.dyndrestkt

import codes.thischwa.dyndrestkt.config.AutoDnsConfig
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration


@SpringBootApplication
@Configuration
@EnableConfigurationProperties(value = [AutoDnsConfig::class])
class DynDRestKtApplication

private val log = KotlinLogging.logger {}

fun main(args: Array<String>) {
    log.info { "Processing start of DynDRest" }
    runApplication<DynDRestKtApplication>(*args)
}
