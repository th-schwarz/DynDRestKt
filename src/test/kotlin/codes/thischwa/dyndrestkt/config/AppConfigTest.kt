package codes.thischwa.dyndrestkt.config

import codes.thischwa.dyndrestkt.GenericIntegrationTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [AppConfig::class] )
class AppConfigTest(private @Autowired val config : AppConfig) : GenericIntegrationTest() {

    @Test
    fun test() {
        assertFalse(config.hostValidationEnabled)
        assertTrue(config.greetingEnabled)

        assertEquals("domainrobot", config.provider)

        assertEquals("file:target/test-classes/test-files/dyndrest-update*", config.updateLogFilePattern)
        assertEquals("(.*)\\s+-\\s+([a-zA-Z\\.-]*)\\s+(\\S*)\\s+(\\S*)", config.updateLogPattern)
        assertEquals("yyyy-MM-dd HH:mm:SSS", config.updateLogDatePattern)
        assertTrue(config.updateLogPageEnabled)
        assertEquals(4, config.updateLogPageSize)
        assertEquals("log-dev", config.updateLogUserName)
        assertEquals("l0g-dev", config.updateLogUserPassword)
    }
}