package codes.thischwa.dyndrestkt.util

import codes.thischwa.dyndrestkt.model.IpSetting
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.xbill.DNS.*
import java.io.IOException
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.charset.StandardCharsets
import java.util.*


class NetUtil {

    companion object {
        fun isIP(ipStr: String?): Boolean {
            return isIPv4(ipStr) || isIPv6(ipStr)
        }

        fun isIPv4(ipStr: String?): Boolean {
            return try {
                InetAddress.getByName(ipStr) is Inet4Address
            } catch (e: UnknownHostException) {
                false
            }
        }

        fun isIPv6(ipStr: String?): Boolean {
            return try {
                InetAddress.getByName(ipStr) is Inet6Address
            } catch (e: UnknownHostException) {
                false
            }
        }

        fun buildBasicAuth(user: String?, pwd: String?): String {
            val authStr = String.format("%s:%s", user, pwd)
            val base64Creds: String = Base64.getEncoder().encodeToString(authStr.toByteArray(StandardCharsets.UTF_8))
            return "Basic $base64Creds"
        }

        fun getBaseUrl(forceHttps: Boolean): String? {
            val builder = ServletUriComponentsBuilder.fromCurrentContextPath()
            if (forceHttps) builder.scheme("https")
            return builder.replacePath(null).build().toUriString()
        }

        /**
         * Resolves the ip settings of the desired 'hostName'.
         *
         * @param hostName the host name
         * @return the ip setting
         * @throws IOException if the resolving fails
         */
        @Throws(IOException::class)
        fun resolve(hostName: String): IpSetting {
            val ipSetting = IpSetting()
            var rec: Record? = lookup(hostName, Type.A)
            if (rec != null) {
                ipSetting.ipv4 = (rec as ARecord).address as Inet4Address
            }
            rec = lookup(hostName, Type.AAAA)
            if (rec != null) ipSetting.ipv6 = (rec as AAAARecord).address as Inet6Address
            return ipSetting
        }

        @Throws(IOException::class)
        private fun lookup(hostName: String, type: Int): Record? {
            return try {
                val records: Array<Record> = Lookup(hostName, type).run()
                if (records.isEmpty()) null else records[0]
            } catch (e: TextParseException) {
                throw IOException(String.format("Couldn't lookup %s", hostName), e)
            }
        }

    }
}