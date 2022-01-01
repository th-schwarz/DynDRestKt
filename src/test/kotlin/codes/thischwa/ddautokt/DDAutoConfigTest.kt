package codes.thischwa.ddautokt

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = [DDAutoConfig::class] )
@ExtendWith(SpringExtension::class)
class DDAutoConfigTest {
    @Autowired
    lateinit var config: DDAutoConfig

    val configuredEntries : Int = 2

    @BeforeEach
    fun setUp() {
        config.read()
    }

    @Test
    fun testCountZones() {
        assertEquals(configuredEntries, config.zones.size)
    }

    @Test
    fun testZoneDetails() {
        val zone: DDAutoConfig.Zone = config.zones[0]
        assertEquals("dynhost0.info", zone.name)
        assertEquals("ns0.domain.info", zone.ns)

        assertEquals("my0:1234567890abcdef", zone.hosts[0])
        assertEquals("test1:1234567890abcdx", zone.hosts[1])
    }

    @Test
    fun testApiToken() {
        assertEquals("1234567890abcdef", config.apiToken("my0.dynhost0.info"))
        assertThrows(IllegalArgumentException::class.java) { config.apiToken("unknown.host.info") }
    }

    @Test
    fun testPrimaryNameServer() {
        assertEquals("ns1.domain.info", config.primaryNameServer("dynhost1.info"))
        assertThrows(IllegalArgumentException::class.java) { config.primaryNameServer("unknown-host.info") }
    }
}