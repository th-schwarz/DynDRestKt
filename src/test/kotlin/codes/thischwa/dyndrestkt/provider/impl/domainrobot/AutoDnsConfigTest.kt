package codes.thischwa.dyndrestkt.provider.impl.domainrobot

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(classes = [AutoDnsConfig::class] )
@ExtendWith(SpringExtension::class)
class AutoDnsConfigTest  {
    @Autowired
    lateinit var config : AutoDnsConfig

    @Test
    fun testConfig() {
        assertEquals("https://api.autodns.com/v1", config.url)
        assertEquals(4, config.context)
        assertEquals("user_t", config.user)
        assertEquals("pwd_t", config.password)
    }
}