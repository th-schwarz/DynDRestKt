package codes.thischwa.dyndrestkt.config

import codes.thischwa.dyndrestkt.GenericIntegrationTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [AppConfig::class] )
class AppConfigTest @Autowired constructor(val config : AppConfig) : GenericIntegrationTest() {

    @Test
    fun test() {
        assertEquals("domainrobot", config.provider)
        assertFalse(config.hostValidationEnabled)
        assertTrue(config.updateLogPageEnabled)
        assertEquals("log-dev", config.updateLogUserName)
        assertEquals("l0g-dev", config.updateLogUserPassword)
    }
}