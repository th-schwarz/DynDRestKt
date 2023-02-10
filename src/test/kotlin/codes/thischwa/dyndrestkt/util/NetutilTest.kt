package codes.thischwa.dyndrestkt.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.IOException


class NetutilTest {

    @Test
    @Throws(IOException::class)
    fun testResolve() {
        val ips = NetUtil.resolve("mein-email-fach.de")
        assertEquals(
            "IpSetting(ipv4=188.68.45.198,ipv6=2a03:4000:41:32:0:0:0:2)",
            ips.toString()
        )
    }

    @Test
    fun testIsIp() {
        assertTrue(NetUtil.isIP("188.68.45.198"))
        assertFalse(NetUtil.isIP("188.68.45.265"))
        assertTrue(NetUtil.isIP("2a03:4000:41:32:0:0:0:2"))
        assertFalse(NetUtil.isIP("2a03:4000:41:32:0:0:0:2h"))
    }

    @Test
    fun testIsIpv4() {
        assertTrue(NetUtil.isIPv4("188.68.45.198"))
        assertFalse(NetUtil.isIPv4("188.68.45.265"))
    }

    @Test
    fun testIsIpv6() {
        assertTrue(NetUtil.isIPv6("2a03:4000:41:32:0:0:0:2"))
        assertFalse(NetUtil.isIPv6("2a03:4000:41:32:0:0:0:2h"))
    }
}