package codes.thischwa.ddautokt.service

import codes.thischwa.ddautokt.AutoDnsConfig
import codes.thischwa.ddautokt.DDAutoConfig
import codes.thischwa.ddautokt.util.ZoneUtil
import mu.KotlinLogging
import org.domainrobot.sdk.client.Domainrobot
import org.domainrobot.sdk.client.clients.ZoneClient
import org.domainrobot.sdk.models.DomainRobotHeaders
import org.domainrobot.sdk.models.DomainrobotApiException
import org.domainrobot.sdk.models.generated.Zone
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ZoneSdk {

    private val log = KotlinLogging.logger {}

    private val customHeaders: Map<String, String> = mapOf(DomainRobotHeaders.DOMAINROBOT_HEADER_WEBSOCKET to "NONE")

    @Autowired
    lateinit var autoDnsConfig : AutoDnsConfig

    @Autowired
    lateinit var config: DDAutoConfig

    private fun getInstance(): ZoneClient {
        return Domainrobot(autoDnsConfig.user, autoDnsConfig.context.toString(), autoDnsConfig.password,
            autoDnsConfig.url).zone
    }

    fun validateConfiguredZones() {
        for (z in config.configuredZones()) {
            val zone: Zone = zoneInfo(z, config.primaryNameServer(z))
            log.info("Zone correct initialized: {}", zone.getOrigin())
        }
    }

    @Throws(ZoneSdkException::class)
    fun zoneInfo(origin: String, primaryNameServer: String): Zone {
        val zc = getInstance()
        return try {
            zc.info(origin, primaryNameServer, customHeaders)
        } catch (e: DomainrobotApiException) {
            throw ZoneSdkException("API exception", e)
        } catch (e: Exception) {
            throw ZoneSdkException("Unknown exception", e)
        }
    }

    @Throws(ZoneSdkException::class, IllegalArgumentException::class)
    fun zoneInfo(host: String): Zone {
        require(config.hostExists(host)) { "Host isn't configured: $host" }
        val zone = ZoneUtil.deriveZone(host)
        val primaryNameServer: String = config.primaryNameServer(zone)
        return zoneInfo(zone, primaryNameServer)
    }


    /**
     * Updates the zone derived from the host. <br></br>
     * The parameters must be validated by the caller.
     *
     * @param host
     * the hostname, should be a sub domain
     * @param ipv4
     * Add or update the ipv4 address. If it's null, it will be dropped from the zone.
     * @param ipv6
     * Add or update the ipv6 address. If it's null, it will be dropped from the zone.
     * @throws ZoneSdkException
     */
    @Throws(ZoneSdkException::class)
    fun zoneUpdate(host: String, ipv4: String?, ipv6: String?) {
        val sld = host.substring(0, host.indexOf("."))

        // set the IPs in the zone object
        val zone = zoneInfo(host)
        if (ipv4 != null) ZoneUtil.addOrUpdateIPv4(zone, sld, ipv4) else ZoneUtil.removeIPv4(zone, sld)
        if (ipv6 != null) ZoneUtil.addOrUpdateIPv6(zone, sld, ipv6) else ZoneUtil.removeIPv6(zone, sld)

        // processing the update
        val zc = getInstance()
        try {
            zc.update(zone, customHeaders)
        } catch (e: DomainrobotApiException) {
            throw ZoneSdkException("API exception", e)
        } catch (e: java.lang.Exception) {
            throw ZoneSdkException("Unknown exception", e)
        }
    }
}