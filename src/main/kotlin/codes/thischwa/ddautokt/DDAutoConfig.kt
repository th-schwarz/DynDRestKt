package codes.thischwa.ddautokt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ddauto")
class DDAutoConfig {
    lateinit var zones: List<Zone>

    class Zone {
        lateinit var name: String
        lateinit var ns: String
        lateinit var hosts: List<String>
    }
}
