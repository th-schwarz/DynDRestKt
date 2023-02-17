package codes.thischwa.dyndrestkt.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.net.InetAddress
import java.net.UnknownHostException


internal class IpSettingTest {

    @Test
    @Throws(UnknownHostException::class)
    fun testConversion() {
        var `is` = IpSetting("198.0.0.1", "2a03:4000:41:32:0:0:0:1")
        assertEquals(`is`.ipv4ToString(), `is`.ipv4!!.hostAddress)
        assertEquals(`is`.ipv6ToString(), `is`.ipv6!!.hostAddress)
        `is` = IpSetting("2a03:4000:41:32:0:0:0:1")
        assertNull(`is`.ipv4ToString())
        assertNull(`is`.ipv4)
        assertEquals(`is`.ipv6ToString(), `is`.ipv6!!.hostAddress)
        assertNull(`is`.ipv4)
        assertEquals(`is`.ipv6ToString(), `is`.ipv6!!.hostAddress)
        `is` = IpSetting("198.0.0.1")
        assertEquals(`is`.ipv4ToString(), `is`.ipv4!!.hostAddress)
        `is` = IpSetting("198.0.0.1")
        assertEquals(`is`.ipv4ToString(), `is`.ipv4!!.hostAddress)
        assertNull(`is`.ipv6ToString())
        assertNull(`is`.ipv6)
    }

    @Test
    @Throws(UnknownHostException::class)
    fun compareIPv6Test() {
        assertEquals(IpSetting("2a03:4000:41:32:0:0:0:1"), IpSetting("2a03:4000:41:32::1"))
    }

    @Test
    @Throws(UnknownHostException::class)
    fun constructorTest() {
        var `is` = IpSetting("198.0.0.1")
        assertNull(`is`.ipv6)
        assertEquals(`is`.ipv4ToString(), `is`.ipv4!!.hostAddress)
        `is` = IpSetting(InetAddress.getByName("198.0.0.2"), InetAddress.getByName("2a03:4000:41:32::2"))
        assertEquals(`is`.ipv4ToString(), `is`.ipv4!!.hostAddress)
        assertEquals(`is`.ipv6ToString(), `is`.ipv6!!.hostAddress)
    }

    @Test
    fun testException() {
        assertThrows(UnknownHostException::class.java) { IpSetting("256.0.0.1", "2a03:4000:41:32:0:0:0:1") }
    }
}