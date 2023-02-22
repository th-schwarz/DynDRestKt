package codes.thischwa.dyndrestkt.provider.impl.domainrobot

import codes.thischwa.dyndrestkt.config.AppConfig
import codes.thischwa.dyndrestkt.provider.Provider
import mu.KotlinLogging
import org.domainrobot.sdk.client.Domainrobot
import org.domainrobot.sdk.models.DomainRobotHeaders
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.function.Consumer


@ConditionalOnProperty(name = ["dyndrest.provider"], havingValue = "domainrobot")
@Component
class DomainRobotConfigurator @Autowired constructor(private var appConfig: AppConfig, private var autoDnsConfig: AutoDnsConfig, var domainRobotConfig: DomainRobotConfig): InitializingBean {

    private val log = KotlinLogging.logger {}


    private val customHeaders: Map<String, String> =
        HashMap(java.util.Map.of(DomainRobotHeaders.DOMAINROBOT_HEADER_WEBSOCKET, "NONE"))

    // <zone, ns>
    private var zoneData: MutableMap<String, String> = HashMap()

    // <fqdn, apitoken>
    private var apitokenData: MutableMap<String, String> = HashMap()


    @Bean
    fun provider(): Provider {
        val zcw = buildZoneClientWrapper()
        return DomainRobotProvider(appConfig, this, zcw)
    }

    fun buildZoneClientWrapper(): ZoneClientWrapper {
        return ZoneClientWrapper(
            Domainrobot(
                autoDnsConfig.user,
                java.lang.String.valueOf(autoDnsConfig.context),
                autoDnsConfig.password,
                autoDnsConfig.url
            ).getZone(), customHeaders, domainRobotConfig.defaultTtl
        )
    }

    override fun afterPropertiesSet() {
        readAndValidate()
        log.info("*** Api-token and zone data are read and validated successful!")
    }

    fun getDefaultTtl(): Long {
        return domainRobotConfig.defaultTtl
    }

    fun getConfiguredHosts(): Set<String> {
        return apitokenData.keys
    }

    fun getConfiguredZones(): Set<String> {
        return zoneData.keys
    }

    fun hostExists(host: String): Boolean {
        return apitokenData.containsKey(host)
    }

    @Throws(IllegalArgumentException::class)
    fun getApitoken(host: String): String? {
        require(hostExists(host)) { "Host isn't configured: $host" }
        return apitokenData[host]
    }

    @Throws(IllegalArgumentException::class)
    fun getPrimaryNameServer(zone: String): String? {
        require(zoneData.containsKey(zone)) { "Zone isn't configured: $zone" }
        return zoneData[zone]
    }


    fun readAndValidate() {
        read()
        validate()
    }

    @Throws(IllegalArgumentException::class)
    fun read() {
        apitokenData = HashMap()
        zoneData = HashMap()
        domainRobotConfig.zones.forEach { zone: DomainRobotConfig.Zone -> readZoneConfig(zone) }
    }

    private fun readZoneConfig(zone: DomainRobotConfig.Zone) {
        zoneData[zone.name] = zone.ns
        val hostRawData: List<String> = zone.hosts
        require(!(hostRawData.isEmpty())) { "Missing host data for: ${zone.name}" }
        hostRawData.forEach(Consumer { hostRaw: String ->
            readHostString(
                hostRaw,
                zone
            )
        })
    }

    private fun readHostString(hostRaw: String, zone: DomainRobotConfig.Zone) {
        val parts = hostRaw.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        require(parts.size == 2) { "The host entry must be in the following format: [sld|:[apitoken], but it was: $hostRaw" }
        // build the fqdn hostname
        val host = java.lang.String.format("%s.%s", parts[0], zone.name)
        apitokenData[host] = parts[1]
    }

    fun validate() {
        require(!(zoneData.isEmpty() || apitokenData.isEmpty())) { "Zone or host data are empty." }
        log.info("*** Configured hosts:")
        apitokenData.keys.forEach(Consumer { host: String? ->
            log.info(
                " - {}",
                host
            )
        })
    }

    // just for testing
    /*fun getDomainRobotConfig(): DomainRobotConfig? {
        return domainRobotConfig
    }*/
}