package codes.thischwa.ddautokt

import codes.thischwa.ddautokt.config.AutoDnsConfig
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(value = [AutoDnsConfig::class])
class DDAutoKtApplication

private val log = KotlinLogging.logger {}

fun main(args: Array<String>) {
	log.info { "Processing start of DDAuto" }
	runApplication<DDAutoKtApplication>(*args)
}
