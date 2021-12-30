package codes.thischwa.ddautokt

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(classes = [DDAutoConfig::class] )
@ExtendWith(SpringExtension::class)
class DDAutoConfigTest {
    @Autowired
    lateinit var config: DDAutoConfig

    @Test
    fun testCountZones() {
        assertEquals(2, config.zones.size)
    }

    @Test
    fun testZoneDetails() {
        val zone: DDAutoConfig.Zone = config.zones[0]
        assertEquals("dynhost0.info", zone.name)
        assertEquals("ns0.domain.info", zone.ns)

        assertEquals("my0:1234567890abcdef", zone.hosts[0])
        assertEquals("test1:1234567890abcdx", zone.hosts[1])
    }
}