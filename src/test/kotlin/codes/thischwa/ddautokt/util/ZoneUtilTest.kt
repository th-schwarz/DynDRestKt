package codes.thischwa.ddautokt.util

import org.domainrobot.sdk.client.JsonUtils
import org.domainrobot.sdk.models.generated.JsonResponseDataZone
import org.domainrobot.sdk.models.generated.Zone
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.InputStream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZoneUtilTest {

    private lateinit var zone: Zone

    private val rrCount : Int = 5

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        val content : String = ZoneUtilTest::class.java.getResource("/zone-info.json").readText()
        val response = JsonUtils.deserialize(content.toByteArray(),
            JsonResponseDataZone::class.java)
        zone = response.data[0]
    }

    @Test
    fun testValidIP() {
        assertTrue(ZoneUtil.isValidateIP("217.229.139.240"))
        assertTrue(ZoneUtil.isValidateIP("2a03:4000:41:32::1"))
        assertFalse(ZoneUtil.isValidateIP("300.229.139.240"))
        assertFalse(ZoneUtil.isValidateIP("2x03:4000:41:32::1"))
    }

    @Test
    fun testIfIPv6() {
        assertTrue(ZoneUtil.isIPv6("2a03:4000:41:32::1"))
        assertFalse(ZoneUtil.isIPv6("2a03.4000:41:32::1"))
        assertFalse(ZoneUtil.isIPv6("217.229.139.240"))
    }

    @Test
    fun testDeriveZone() {
        assertEquals("example.com", ZoneUtil.deriveZone("sub.example.com"))
    }

    @Test
    fun testUpdateIPv4() {
        assertEquals(rrCount, zone.resourceRecords.size)
        ZoneUtil.addOrUpdateIPv4(zone, "sub", "128.0.0.1")
        assertEquals(rrCount, zone.resourceRecords.size)
        val rr = ZoneUtil.searchResourceRecord(zone, "sub", ZoneUtil.RR_A)
        assertNotNull(rr)
        assertEquals("128.0.0.1", rr!!.value)
    }

    @Test
    fun testUpdateIPv6() {
        assertEquals(rrCount, zone.resourceRecords.size)
        ZoneUtil.addOrUpdateIPv6(zone, "sub", "2a03:4000:41:32::20")
        assertEquals(rrCount, zone.resourceRecords.size)
        val rr = ZoneUtil.searchResourceRecord(zone, "sub", ZoneUtil.RR_AAAA)
        assertNotNull(rr)
        assertEquals("2a03:4000:41:32::20", rr!!.value)
    }

    @Test
    fun testAddIPv4() {
        assertEquals(rrCount, zone.resourceRecords.size)
        ZoneUtil.addOrUpdateIPv4(zone, "sub1", "128.0.0.1")
        assertEquals(rrCount + 1, zone.resourceRecords.size)
        val rr = ZoneUtil.searchResourceRecord(zone, "sub1", ZoneUtil.RR_A)
        assertNotNull(rr)
        assertEquals("128.0.0.1", rr!!.value)
    }

    @Test
    fun testAddIPv6() {
        assertEquals(rrCount, zone.resourceRecords.size)
        ZoneUtil.addOrUpdateIPv6(zone, "sub1", "2a03:4000:41:32::20")
        assertEquals(rrCount + 1, zone.resourceRecords.size)
        val rr = ZoneUtil.searchResourceRecord(zone, "sub1", ZoneUtil.RR_AAAA)
        assertNotNull(rr)
        assertEquals("2a03:4000:41:32::20", rr!!.value)
    }

    @Test
    fun testRemoveIPv4() {
        assertEquals(rrCount, zone.resourceRecords.size)
        ZoneUtil.addOrUpdateIPv4(zone, "sub2", "128.0.0.2")
        assertEquals(rrCount + 1, zone.resourceRecords.size)
        ZoneUtil.removeIPv4(zone, "sub2")
        assertEquals(rrCount, zone.resourceRecords.size)
        ZoneUtil.removeIPv4(zone, "unknownsub")
        assertEquals(rrCount, zone.resourceRecords.size)
    }

    @Test
    fun testRemoveIPv6() {
        assertEquals(rrCount, zone.resourceRecords.size)
        ZoneUtil.addOrUpdateIPv6(zone, "sub2", "2a03:4000:41:32::20")
        assertEquals(rrCount + 1, zone.resourceRecords.size)
        ZoneUtil.removeIPv6(zone, "sub2")
        assertEquals(rrCount, zone.resourceRecords.size)
        ZoneUtil.removeIPv6(zone, "unknownsub")
        assertEquals(rrCount, zone.resourceRecords.size)
    }

}