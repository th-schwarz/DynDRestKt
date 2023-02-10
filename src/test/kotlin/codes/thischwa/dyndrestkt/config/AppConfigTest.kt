package codes.thischwa.dyndrestkt.config

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(classes = [AppConfig::class] )
@ExtendWith(SpringExtension::class)
class AppConfigTest {

    @Autowired
    lateinit var config : AppConfig


    @Test
    fun test() {
        assertEquals("domainrobot", config.provider)
        assertFalse(config.hostValidationEnabled)
        assertTrue(config.updateLogPageEnabled)
        assertEquals("log-dev", config.updateLogUserName)
        assertEquals("l0g-dev", config.updateLogUserPassword)
    }
}