package codes.thischwa.ddautokt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "autodns")
data class AutoDnsConfig (
    var password: String = "",
    var url: String = "",
    var context : Int = 0,
    var user : String = ""
    )