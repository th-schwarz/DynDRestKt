package codes.thischwa.dyndrestkt.provider.impl.domainrobot

import codes.thischwa.dyndrestkt.GenericIntegrationTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [ZoneHostData::class] )
internal class ZoneHostDataTest @Autowired constructor(val data: ZoneHostData): GenericIntegrationTest() {

    private val configuredEntries = 2

    @Test
    fun testCountZones() {
        Assertions.assertEquals(configuredEntries, data.zones.size)
    }

    @Test
    fun testZoneDetails() {
        val zone: Zone = data.zones.get(0)
        Assertions.assertEquals("dynhost0.info", zone.name)
        Assertions.assertEquals("ns0.domain.info", zone.ns)
        Assertions.assertEquals("my0:1234567890abcdef", zone.hosts.get(0))
        Assertions.assertEquals("test0:1234567890abcdx", zone.hosts.get(1))
    }

}