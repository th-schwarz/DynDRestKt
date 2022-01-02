package codes.thischwa.ddautokt.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ZoneUtilTest {

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


}