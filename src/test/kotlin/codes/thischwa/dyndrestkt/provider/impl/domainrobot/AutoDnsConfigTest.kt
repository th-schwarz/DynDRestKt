package codes.thischwa.dyndrestkt.provider.impl.domainrobot

import codes.thischwa.dyndrestkt.GenericIntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [AutoDnsConfig::class] )
class AutoDnsConfigTest(private @Autowired val config : AutoDnsConfig): GenericIntegrationTest() {

    @Test
    fun testConfig() {
        assertEquals("https://api.autodns.com/v1", config.url)
        assertEquals(4, config.context)
        assertEquals("user_t", config.user)
        assertEquals("pwd_t", config.password)
    }
}