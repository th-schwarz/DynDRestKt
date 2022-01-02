package codes.thischwa.ddautokt.util

import java.net.Inet6Address
import java.net.InetAddress
import java.net.UnknownHostException


class ZoneUtil {

    private val DEFAULT_TLD: Long = 60

    companion object {

        val RR_A = "A"

        val RR_AAAA = "AAAA"

        fun deriveZone(host: String): String? {
            val cnt = host.chars().filter { ch: Int -> ch == '.'.code }.count()
            require(cnt >= 2) { "'host' must be a sub domain." }
            return host.substring(host.indexOf(".") + 1)
        }


        fun isIPv4(ipStr: String): Boolean {
            return try {
                InetAddress.getByName(ipStr) is InetAddress
            } catch (e: UnknownHostException) {
                false
            }
        }

        fun isIPv6(ipStr: String): Boolean {
            return try {
                InetAddress.getByName(ipStr) is Inet6Address
            } catch (e: UnknownHostException) {
                false
            }
        }

        fun isValidateIP(ipStr: String): Boolean {
            return isIPv4(ipStr) || isIPv6(ipStr)
        }
    }
}