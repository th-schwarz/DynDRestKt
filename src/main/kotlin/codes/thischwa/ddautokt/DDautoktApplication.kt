package codes.thischwa.ddautokt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["codes.thischwa"])
class DDautoktApplication

fun main(args: Array<String>) {
	runApplication<DDautoktApplication>(*args)
}
