package codes.thischwa.dyndrestkt.provider.impl.domainrobot

import codes.thischwa.dyndrestkt.config.AppConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [AppConfig::class, AutoDnsConfig::class, DomainRobotConfig::class, DomainRobotConfigurator::class])
class DomainRobotConfiguratorTest(@Autowired private var config: DomainRobotConfigurator) {

    private val configuredEntries = 2

    @BeforeEach
    fun setUp() {
        config.read()
    }

    @Test
    fun testDefaulTtl() {
        assertEquals(61L, config.getDefaultTtl())
    }

    @Test
    fun testGetApiToken() {
        assertEquals("1234567890abcdef", config.getApitoken("my0.dynhost0.info"))
        assertThrows(IllegalArgumentException::class.java) {
            config.getApitoken(
                "unknown.host.info"
            )
        }
    }

    @Test
    fun testgetPrimaryNameServer() {
        assertEquals("ns1.domain.info", config.getPrimaryNameServer("dynhost1.info"))
        assertThrows(IllegalArgumentException::class.java) {
            config.getPrimaryNameServer(
                "unknown-host.info"
            )
        }
    }

    @Test
    fun testConfigured() {
        assertEquals(configuredEntries * 2, config.getConfiguredHosts().size)
        assertEquals(configuredEntries, config.getConfiguredZones().size)
    }

    @Test
    fun testValidateData_ok() {
        config.validate()
    }

    @Test
    fun testWrongHostFormat() {
        val wrongHost = "wrong-formatted.host"
        val z: DomainRobotConfig.Zone = config.domainRobotConfig.zones.get(0)
        z.hosts.add(wrongHost)
        assertThrows(IllegalArgumentException::class.java, config::read)
        z.hosts.remove(wrongHost)
    }

    @Test
    fun testEmptyHosts() {
        val z: DomainRobotConfig.Zone = config.domainRobotConfig.zones.get(1)
        val hosts: MutableList<String> = ArrayList(z.hosts)
        z.hosts.clear()
        assertThrows(IllegalArgumentException::class.java, config::read)
        z.hosts = hosts
    }
}