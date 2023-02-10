package codes.thischwa.dyndrestkt.model

import com.fasterxml.jackson.annotation.JsonGetter
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.Objects


class IpSetting {

    var ipv4: Inet4Address? = null
    var ipv6: Inet6Address? = null

    @Throws(UnknownHostException::class)
    constructor(ipv4Str: String?, ipv6Str: String?) {
        if (ipv4Str != null) ipv4 = InetAddress.getByName(ipv4Str) as Inet4Address?
        if (ipv6Str != null) ipv6 = InetAddress.getByName(ipv6Str) as Inet6Address?
    }

    constructor(ipv4: InetAddress?, ipv6: InetAddress?) {
        if (ipv4 is Inet4Address) this.ipv4 = ipv4
        if (ipv6 is Inet6Address) this.ipv6 = ipv6
    }

    constructor()

    @Throws(UnknownHostException::class)
    constructor(ipStr: String?) {
        val ip: InetAddress = InetAddress.getByName(ipStr)
        if (ip is Inet4Address)
            ipv4 = ip
        else
            ipv6 = ip as Inet6Address
    }


    fun isNotSet(): Boolean {
        return ipv4 == null && ipv6 == null
    }

    @JsonGetter("ipv4")
    fun ipv4ToString(): String? {
        return if (ipv4 == null) null else ipv4!!.hostAddress
    }

    @JsonGetter("ipv6")
    fun ipv6ToString(): String? {
        return if (ipv6 == null) null else ipv6!!.hostAddress
    }

    override fun toString(): String {
        val ipv4Str: String = if (ipv4 != null) ipv4!!.hostAddress else "n/a"
        val ipv6Str: String = if (ipv6 != null) ipv6!!.hostAddress else "n/a"
        return "IpSetting(ipv4=${ipv4Str},ipv6=${ipv6Str})"
    }

    override fun hashCode(): Int {
        return Objects.hash(ipv4, ipv6)
    }

    override fun equals(other: Any?): Boolean {
        return (other is IpSetting)
                && Objects.equals(ipv4, other.ipv4) && Objects.equals(ipv6, other.ipv6)
    }
}