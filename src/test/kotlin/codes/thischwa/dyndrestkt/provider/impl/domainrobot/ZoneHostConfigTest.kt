package codes.thischwa.dyndrestkt.provider.impl.domainrobot

import codes.thischwa.dyndrestkt.GenericIntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertFalse
import kotlin.test.assertTrue


@SpringBootTest(classes = [ZoneHostData::class] )
internal class ZoneHostConfigTest @Autowired constructor(data: ZoneHostData): GenericIntegrationTest() {

    private val configuredEntries = 2

    private val config = ZoneHostConfig(data)

    @BeforeEach
    fun setUp() {
        config.read()
    }

    @Test
    fun testCountZones() {
        assertEquals(configuredEntries, config.zoneData.size)
    }

    @Test
    fun getConfiguredHosts() {
        assertEquals(configuredEntries * 2, config.getConfiguredHosts().size)
    }

    @Test
    fun getConfiguredZones() {
        assertEquals(configuredEntries, config.getConfiguredZones().size)
    }

    @Test
    fun hostExists() {
        assertTrue { config.hostExists("my0.dynhost0.info") }
        assertFalse { config.hostExists("unknown-host.info") }
    }

    @Test
    fun getApitoken() {
        assertEquals("1234567890abcdef", config.getApitoken("my0.dynhost0.info"))
        assertThrows(
            IllegalArgumentException::class.java
        ) { config.getApitoken("unknown.host.info") }
    }

    @Test
    fun getPrimaryNameServer() {
        assertEquals("ns1.domain.info", config.getPrimaryNameServer("dynhost1.info"))
        assertThrows(
            IllegalArgumentException::class.java
        ) { config.getPrimaryNameServer("unknown-host.info") }
    }
}