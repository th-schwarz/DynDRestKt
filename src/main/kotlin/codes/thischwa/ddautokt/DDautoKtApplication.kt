package codes.thischwa.ddautokt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(value = [AutoDnsConfig::class])
class DDautoKtApplication

fun main(args: Array<String>) {
	runApplication<DDautoKtApplication>(*args)
}
