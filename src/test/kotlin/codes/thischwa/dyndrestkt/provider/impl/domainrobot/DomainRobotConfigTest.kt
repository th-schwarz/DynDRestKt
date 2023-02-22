package codes.thischwa.dyndrestkt.provider.impl.domainrobot

import codes.thischwa.dyndrestkt.GenericIntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [DomainRobotConfig::class] )
internal class DomainRobotConfigTest(@Autowired private var config: DomainRobotConfig): GenericIntegrationTest() {

    private val configuredEntries = 2

    @Test
    fun testCountZones() {
        assertEquals(configuredEntries, config.zones.size)
    }

    @Test
    fun testZoneDetails() {
        val zone: DomainRobotConfig.Zone = config.zones.get(0)
        assertEquals("dynhost0.info", zone.name)
        assertEquals("ns0.domain.info", zone.ns)
        assertEquals("my0:1234567890abcdef", zone.hosts.get(0))
        assertEquals("test0:1234567890abcdx", zone.hosts.get(1))
    }
}