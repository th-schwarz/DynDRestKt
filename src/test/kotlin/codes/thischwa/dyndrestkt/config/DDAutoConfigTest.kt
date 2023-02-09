package codes.thischwa.dyndrestkt.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class DDAutoConfigTest {

    @Autowired
    lateinit var config: DDAutoConfig

    val configuredEntries: Int = 2

    @BeforeEach
    fun setUp() {
        config.read()
    }

    @Test
    fun testCountZones() {
        assertEquals(configuredEntries, config.data.zones.size)
    }

    @Test
    fun testZoneDetails() {
        val zone: DDAutoConfigData.Zone = config.data.zones[0]
        assertEquals("dynhost0.info", zone.name)
        assertEquals("ns0.domain.info", zone.ns)

        assertEquals("my0:1234567890abcdef", zone.hosts[0])
        assertEquals("test0:1234567890abcdx", zone.hosts[1])
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

    @Test
    fun testConfigured() {
        assertEquals(configuredEntries*2, config.configuredHosts().size)
        assertEquals(configuredEntries, config.configuredZones().size)
    }

    @Test
    fun validation_ok() {
        config.validate()
    }

    @Test
    fun testWrongHostFormat() {
        val wrongHost = "wrong-formatted.host"
        val z : DDAutoConfigData.Zone = config.data.zones[0]
        z.hosts.add(wrongHost)
        assertThrows(IllegalArgumentException::class.java) { config.read() }
        z.hosts.remove(wrongHost)
    }

    @Test
    fun testEmptyHosts() {
        val z  : DDAutoConfigData.Zone = config.data.zones[1]
        val hosts = ArrayList(z.hosts)
        z.hosts.clear()
        assertThrows(IllegalArgumentException::class.java) { config.read() }
        z.hosts = hosts
    }
}