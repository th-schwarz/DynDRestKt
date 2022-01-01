package codes.thischwa.ddautokt

import mu.KotlinLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ddauto")
class DDAutoConfig {

    lateinit var zones: List<Zone>

    private val log = KotlinLogging.logger {}

    // <zone, ns>
    private var zoneData = mutableMapOf<String, String>()

    // <fqdn, apitoken>
    private var apitokenData  = mutableMapOf<String, String>()

    fun configuredHosts() : Set<String> = apitokenData.keys

    fun configuredZones() : Set<String> = zoneData.keys

    fun hostExists(host : String) = apitokenData.containsKey(host)

    @Throws(IllegalArgumentException::class)
    fun apiToken(host : String) : String {
        if (!hostExists(host))
            throw IllegalArgumentException("Host isn't configured: $host")
        return apitokenData[host]!!
    }

    @Throws(IllegalArgumentException::class)
    fun primaryNameServer(zone : String) : String {
        if(!zoneData.containsKey(zone))
            throw IllegalArgumentException("Zone isn't configured: $zone")
        return zoneData[zone]!!
    }

    fun eadAndValidate() {
        read()
        validate()
    }

    @Throws(IllegalArgumentException::class)
    fun validate() {
        if (zoneData.isEmpty() || apitokenData.isEmpty())
            throw IllegalArgumentException("Zone or host data is empty.")
        log.info { "*** Configured hosts:" }
        for (host in configuredHosts())
            log.info { "  - $host" }
    }

    @Throws(IllegalArgumentException::class)
    fun read() {
        for (zone in zones) {
            zoneData[zone.name]= zone.ns
            var hostData = zone.hosts
            if (hostData.isEmpty())
                throw IllegalArgumentException("Missing host data for $zone")
            for (hostToken in hostData) {
                val partsAry = hostToken.split(":")
                if (partsAry.size != 2)
                    throw IllegalArgumentException("The host data must be in the following format: [sld]:[apitoken], but it was $hostToken")
                // build the fqdn hostname
                val host = partsAry[0] + "." + zone.name
                apitokenData[host] = partsAry[1]
            }
        }
    }

    class Zone {
        lateinit var name: String
        lateinit var ns: String
        lateinit var hosts: List<String>
    }
}
