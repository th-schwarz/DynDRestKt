package codes.thischwa.ddautokt

import mu.KotlinLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Primary
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ddauto")
class DDAutoConfig : BeanInitialisation {

    lateinit var zones: List<Zone>

    private val log = KotlinLogging.logger {}

    // <zone, ns>
    private var zoneData = mutableMapOf<String, String>()

    // <fqdn, apitoken>
    private var apitokenData  = mutableMapOf<String, String>()

    override fun initMe() {
        read()
        validate()
    }

    fun configuredHosts() : Set<String> = apitokenData.keys

    fun configuredZones() : Set<String> = zoneData.keys

    fun hostExists(host : String) = apitokenData.containsKey(host)

    fun apiToken(host : String) : String {
        require (hostExists(host)) {"Host isn't configured: $host"}
        return apitokenData[host]!!
    }

    fun primaryNameServer(zone : String) : String {
        require(zoneData.containsKey(zone)) { "Zone isn't configured: $zone" }
        return zoneData[zone]!!
    }

    fun validate() {
        require (zoneData.isEmpty().not() && apitokenData.isEmpty().not()) {"Zone or host data is empty."}
        log.info { "*** Configured hosts:" }
        for (host in configuredHosts())
            log.info { "  - $host" }
    }

    fun read() {
        for (zone in zones) {
            zoneData[zone.name]= zone.ns
            require (zone.hosts.isEmpty().not()) {"Missing host data for $zone"}
              for (hostToken in zone.hosts) {
                val partsAry = hostToken.split(":")
                require (partsAry.size == 2) {"The host data must be in the following format: [sld]:[apitoken], but it was $hostToken"}
                // build the fqdn hostname
                val host = partsAry[0] + "." + zone.name
                apitokenData[host] = partsAry[1]
            }
        }
    }

    class Zone {
        lateinit var name: String
        lateinit var ns: String
        lateinit var hosts: ArrayList<String>  // not List because of testing
    }
}
