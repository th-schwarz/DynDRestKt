package codes.thischwa.ddautokt

import codes.thischwa.ddautokt.config.DDAutoConfig
import codes.thischwa.ddautokt.service.ZoneSdk
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    private val log = KotlinLogging.logger {}

    @Autowired
    lateinit var conf: DDAutoConfig

    @Autowired
    lateinit var sdk : ZoneSdk

    @GetMapping(value = ["/exist/{host}"], produces = [MediaType.TEXT_HTML_VALUE])
    fun exist(@PathVariable host: String): ResponseEntity<String> {
        log.debug("entered #exist: host={}", host)
        return if (conf.hostExists(host)) ResponseEntity.ok("Host found.") else ResponseEntity("Host not found: $host",
            HttpStatus.NOT_FOUND)
    }

}