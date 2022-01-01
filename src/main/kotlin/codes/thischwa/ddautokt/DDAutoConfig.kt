package codes.thischwa.ddautokt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ddauto")
class DDAutoConfig {
    lateinit var zones: List<Zone>

    // <zone, ns>
    private var zoneData : Map<String, String> = mutableMapOf()

    // <fqdn, apitoken>
    private var apitokenData : Map<String, String> = mutableMapOf()

    fun configuredHosts() : Set<String> = apitokenData.keys

    fun configuredZones() : Set<String> = zoneData.keys

    fun hostExists(host : String) = apitokenData.containsKey(host)

    @Throws(IllegalArgumentException::class)
    fun apiToken(host : String) : String {
        if (!hostExists(host))
            throw IllegalArgumentException("Host isn't configured: " + host)
        return apitokenData[host]!!
    }

    @Throws(IllegalArgumentException::class)
    fun primaryNameServer(zone : String) : String {
        if(!zoneData.containsKey(zone))
            throw IllegalArgumentException("Zone isn't configured: " + zone)
        return zoneData[zone]!!
    }

    class Zone {
        lateinit var name: String
        lateinit var ns: String
        lateinit var hosts: List<String>
    }
}
