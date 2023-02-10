package codes.thischwa.dyndrestkt.provider.impl.domainrobot

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration


/**
 * Holds the AutoDNS base url and credentials for the Domainrobot Sdk.
 */
@ConditionalOnProperty(name = arrayOf("dyndrest.provider"), havingValue = "domainrobot")
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "domainrobot.autodns")
class AutoDnsConfig {
    lateinit var password: String
    lateinit var url: String
    var context: Int = 0
    lateinit var user : String
}