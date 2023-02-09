package codes.thischwa.dyndrestkt.config

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DDAutoConfig : BeanInitializer {

    private val log = KotlinLogging.logger {}

    @Autowired
    lateinit var data: DDAutoConfigData

    override fun doInitialize() {
        read()
        validate()
    }

    // <zone, ns>
    private var zoneData = mutableMapOf<String, String>()

    // <fqdn, apitoken>
    private var apitokenData = mutableMapOf<String, String>()

    fun configuredHosts(): Set<String> = apitokenData.keys

    fun configuredZones(): Set<String> = zoneData.keys

    fun hostExists(host: String) = apitokenData.containsKey(host)

    fun apiToken(host: String): String {
        require(hostExists(host)) { "Host isn't configured: $host" }
        return apitokenData[host]!!
    }

    fun primaryNameServer(zone: String): String {
        require(zoneData.containsKey(zone)) { "Zone isn't configured: $zone" }
        return zoneData[zone]!!
    }

    fun validate() {
        require(zoneData.isEmpty().not() && apitokenData.isEmpty().not()) { "Zone or host data is empty." }
        log.info { "*** Configured hosts:" }
        for (host in configuredHosts())
            log.info { "  - $host" }
    }

    fun read() {
        for (zone in data.zones) {
            zoneData[zone.name] = zone.ns
            require(zone.hosts.isEmpty().not()) { "Missing host data for $zone" }
            for (hostToken in zone.hosts) {
                val partsAry = hostToken.split(":")
                require(partsAry.size == 2) { "The host data must be in the following format: [sld]:[apitoken], but it was $hostToken" }
                // build the fqdn hostname
                val host = partsAry[0] + "." + zone.name
                apitokenData[host] = partsAry[1]
            }
        }
    }
}