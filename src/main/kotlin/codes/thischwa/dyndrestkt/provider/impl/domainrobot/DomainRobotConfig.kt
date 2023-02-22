package codes.thischwa.dyndrestkt.provider.impl.domainrobot

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(name = ["dyndrest.provider"], havingValue = "domainrobot")
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "domainrobot")
class DomainRobotConfig {

    var defaultTtl: Long = 60
    lateinit var zones: MutableList<Zone>

    class Zone {
        lateinit var name: String
        lateinit var ns: String
        lateinit var hosts: MutableList<String>
    }

}