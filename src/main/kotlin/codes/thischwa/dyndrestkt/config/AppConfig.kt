package codes.thischwa.dyndrestkt.config

import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "dyndrest")
class AppConfig : InitializingBean {

    private val log = KotlinLogging.logger {}

    lateinit var provider: String
    lateinit var updateLogFilePattern: String
    var updateLogPageSize: Int = 30
    lateinit var updateLogPattern: String
    lateinit var updateLogDatePattern: String
    var updateLogPageEnabled: Boolean = false
    var hostValidationEnabled: Boolean = true
    lateinit var updateLogUserName: String
    lateinit var updateLogUserPassword: String
    var updateLogRestForceHttps: Boolean = false
    var greetingEnabled: Boolean = false

    override fun afterPropertiesSet() {
        log.info("*** Setting for DynDRest:")
        log.info("  * provider: {}", provider)
        log.info("  * greeting-enabled: {}", greetingEnabled)
        log.info("  * host-validation-enabled: {}", hostValidationEnabled)
        log.info("  * update-log-page-enabled: {}", updateLogPageEnabled)
    }
}