package codes.thischwa.dyndrestkt.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Primary
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ddauto")
class DDAutoConfigData {
    lateinit var zones: List<Zone>

    class Zone {
        lateinit var name: String
        lateinit var ns: String
        lateinit var hosts: ArrayList<String>  // not List because of testing
    }
}