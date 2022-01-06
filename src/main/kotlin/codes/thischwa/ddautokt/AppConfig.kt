package codes.thischwa.ddautokt

import codes.thischwa.ddautokt.config.BeanInitializer
import codes.thischwa.ddautokt.service.ZoneSdk
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component


@Configuration
class AppConfig {

    private val log = KotlinLogging.logger {}

    @Value("\${zone.validation.enabled:true}")
    private val zoneValidation = false

    @Bean
    fun createApplicationReadyListener(sdk: ZoneSdk): ApplicationListener<ApplicationReadyEvent?>? {
        return ApplicationListener { e: ApplicationReadyEvent? ->
            if (zoneValidation) {
                log.debug("Process zone-validation ...")
                sdk.validateConfiguredZones()
            } else {
                log.debug("Zone validation isn't set, no validation processed.")
            }
        }
    }
}

@Component
class AppEventListener : ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val ctx = event.applicationContext
        for (bean in ctx.getBeansOfType(BeanInitializer::class.java).values) {
            bean.doInitialize()
        }
    }
}
