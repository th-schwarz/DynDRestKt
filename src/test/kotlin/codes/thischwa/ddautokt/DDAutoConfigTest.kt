package codes.thischwa.ddautokt

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(classes = [DDAutoConfig::class] )
@ExtendWith(SpringExtension::class)
class DDAutoConfigTest  {
    @Autowired
    lateinit var config : DDAutoConfig

    @Test
    fun testCountZones() {
        assertEquals(2, config.zones.size)
    }
}