package codes.thischwa.dyndrestkt.provider.impl.domainrobot

import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import java.util.function.Consumer

class ZoneHostConfig(val zoneHostData: ZoneHostData): InitializingBean {

    private val log = KotlinLogging.logger {}

    // <zone, ns>
    var zoneData = HashMap<String, String>()
        private set

    // <fqdn, apitoken>
    var apitokenData: MutableMap<String, String> = HashMap()
        private set

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

    override fun afterPropertiesSet() {
        readAndValidate()
        log.info("*** Api-token and zone data are read and validated successful!")
    }

    fun readAndValidate() {
        read()
        validate()
    }

    @Throws(IllegalArgumentException::class)
    fun read() {
        apitokenData.clear()
        zoneData.clear()
        zoneHostData.zones.forEach(Consumer { zone: Zone ->
            readZoneConfig(
                zone
            )
        })
    }

    private fun readZoneConfig(zone: Zone) {
        zoneData[zone.name] = zone.ns
        val hostRawData: List<String> = zone.hosts
        require(!hostRawData.isEmpty()) { "Missing host data for: " + zone.name }
        hostRawData.forEach(Consumer { hostRaw: String? ->
            readHostString(
                hostRaw!!,
                zone
            )
        })
    }

    private fun readHostString(hostRaw: String, zone: Zone) {
        val parts = hostRaw.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        require(parts.size == 2) { "The host entry must be in the following format: [sld|:[apitoken], but it was: $hostRaw" }
        // build the fqdn hostname
        val host = String.format("%s.%s", parts[0], zone.name)
        apitokenData[host] = parts[1]
    }

    fun validate() {
        require(!(zoneData.isEmpty() || apitokenData.isEmpty())) { "Zone or host data are empty." }
        log.info("*** Configured hosts:")
        apitokenData.keys.forEach { host -> log.info(" - {}", host) }
    }

}