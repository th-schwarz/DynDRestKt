package codes.thischwa.dyndrestkt.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "autodns")
class AutoDnsConfig {
    lateinit var password: String
    lateinit var url: String
    var context: Int = 0
    lateinit var user : String
}