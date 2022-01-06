package codes.thischwa.ddautokt

import codes.thischwa.ddautokt.config.DDAutoConfig
import codes.thischwa.ddautokt.service.ZoneSdk
import codes.thischwa.ddautokt.service.ZoneSdkException
import codes.thischwa.ddautokt.util.ZoneUtil
import mu.KotlinLogging
import org.domainrobot.sdk.models.generated.ResourceRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


@RestController
class MainController {

    private val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var conf: DDAutoConfig

    @Autowired
    private lateinit var sdk : ZoneSdk

    @GetMapping(value = ["/exist/{host}"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun exist(@PathVariable host: String): ResponseEntity<String> {
        logger.debug("entered #exist: host={}", host)
        return if (conf.hostExists(host)) ResponseEntity.ok("Host found.") else ResponseEntity("Host not found: $host",
            HttpStatus.NOT_FOUND)
    }


    @GetMapping(value = ["/update/{host}"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun update(
        @PathVariable host: String,
        @RequestParam apitoken: String,
        @RequestParam(name = "ipv4", required = false) ipv4: String?,
        @RequestParam(name = "ipv6", required = false) ipv6: String?,
        req: HttpServletRequest,
    ): ResponseEntity<String?>? {
        var ipv4Str = ipv4
        var ipv6Str = ipv6
        logger.debug("entered #update: host={}, apitoken={}, ipv4={}, ipv6={}", host, apitoken, ipv4Str, ipv6Str)

        // validation
        if (!conf.hostExists(host)) return ResponseEntity("Host not found!", HttpStatus.NOT_FOUND)
        val validApitoken: String = conf.apiToken(host)
        if (validApitoken != apitoken) return ResponseEntity("Stop processing, unknown 'apitoken': $apitoken",
            HttpStatus.BAD_REQUEST)
        if (ipv4Str != null && !ZoneUtil.isIPv4(ipv4Str)) return ResponseEntity("Request parameter 'ipv4' isn't valid: $ipv4Str",
            HttpStatus.BAD_REQUEST)
        if (ipv6Str != null && !ZoneUtil.isIPv6(ipv6Str)) return ResponseEntity("Request parameter 'ipv6' isn't valid: $ipv6Str",
            HttpStatus.BAD_REQUEST)
        if (ipv4Str == null && ipv6Str == null) {
            logger.debug("Both IP parameters are null, try to fetch the remote IP.")
            val remoteIP = req.remoteAddr
            if (remoteIP == null) {
                logger.error("Couldn't determine the remote ip!")
                return ResponseEntity("Couldn't determine the remote ip!", HttpStatus.BAD_REQUEST)
            }
            logger.debug("Fetched remote IP: {}", remoteIP)
            if (ZoneUtil.isIPv6(remoteIP)) ipv6Str = remoteIP else ipv4Str = remoteIP
        }

        // processing the update
        try {
            sdk.zoneUpdate(host, ipv4Str, ipv6Str)
            logger.info("Updated host {} successful with ipv4={}, ipv6={}", host, ipv4Str, ipv6Str)
            //updateLogger.log(host, ipv4Str, ipv6Str)
        } catch (e: ZoneSdkException) {
            logger.error("Updated host failed: $host", e)
            return ResponseEntity("Update failed!", HttpStatus.INTERNAL_SERVER_ERROR)
        } /*catch (e: UpdateLoggerException) {
            logger.error("Error while writing to zone log.", e)
        }*/
        return ResponseEntity.ok("Update successful.")
    }


    @GetMapping(value = ["/info/{host}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun info(@PathVariable host: String): ResponseEntity<String?>? {
        logger.debug("entered #info: host={}", host)
        if (!conf.hostExists(host)) return ResponseEntity("Host not found.", HttpStatus.NOT_FOUND)
        val zone = try {
            sdk.zoneInfo(host)
        } catch (e: ZoneSdkException) {
            logger.error("Zone info failed for: $host", e)
            return ResponseEntity("Zone info failed.", HttpStatus.INTERNAL_SERVER_ERROR)
        }
        val sld = host.substring(0, host.indexOf("."))
        var rr: ResourceRecord? = ZoneUtil.searchResourceRecord(zone, sld, ZoneUtil.RR_A)
        val ipv4Str = if (rr == null) "n/a" else rr.value
        rr = ZoneUtil.searchResourceRecord(zone, sld, ZoneUtil.RR_AAAA)
        val ipv6Str = if (rr == null) "n/a" else rr.value
        val info = StringBuilder()
        info.append("IP settings for host: ").append(host).append('\n')
        info.append("IPv4: ").append(ipv4Str).append('\n')
        info.append("IPv6: ").append(ipv6Str).append('\n')
        return ResponseEntity.ok(info.toString())
    }


    @GetMapping(value = ["meminfo"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun getMemoryStatistics(): ResponseEntity<String?>? {
        val memInfo = StringBuilder()
        memInfo.append("Basic memory Information:\n")
        memInfo.append(String.format("Total: %6d MB\n", Runtime.getRuntime().totalMemory() / (1024L * 1024L)))
        memInfo.append(String.format("Max:   %6d MB\n", Runtime.getRuntime().maxMemory() / (1024L * 1024L)))
        memInfo.append(String.format("Free:  %6d MB\n", Runtime.getRuntime().freeMemory() / (1024L * 1024L)))
        return ResponseEntity.ok(memInfo.toString())
    }
}