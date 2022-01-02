package codes.thischwa.ddautokt

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(value = [AutoDnsConfig::class, DDAutoConfig::class])
class DDAutoKtApplication

private val log = KotlinLogging.logger {}

fun main(args: Array<String>) {
	log.info { "start processed" }
    runApplication<DDAutoKtApplication>(*args)
}
