package codes.thischwa.ddautokt

import codes.thischwa.ddautokt.config.BeanInitializer
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class AppEventListener : ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val ctx = event.applicationContext
        for (bean in ctx.getBeansOfType(BeanInitializer::class.java).values) {
            bean.doInitialize()
        }
    }
}
