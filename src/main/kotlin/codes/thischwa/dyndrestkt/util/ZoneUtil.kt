package codes.thischwa.dyndrestkt.util

import org.domainrobot.sdk.models.generated.ResourceRecord
import org.domainrobot.sdk.models.generated.Zone
import java.net.Inet6Address
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*


class ZoneUtil {

    companion object {

        private val DEFAULT_TLD: Long = 60

        var RR_A = "A"

        var RR_AAAA = "AAAA"

        fun deriveZone(host: String): String {
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

        fun addOrUpdateIPv4(zone: Zone, sld: String, ip: String) {
            addOrUpdateIP(zone, sld, ip, RR_A)
        }

        fun addOrUpdateIPv6(zone: Zone, sld: String, ip: String) {
            addOrUpdateIP(zone, sld, ip, RR_AAAA)
        }

        private fun addOrUpdateIP(zone: Zone, sld: String, ip: String, type: String) {
            val rr : ResourceRecord? = searchResourceRecord(zone, sld, type)
            if (rr != null) {
                rr.value = ip
                rr.ttl = DEFAULT_TLD
            } else {
                val rrSld = ResourceRecord()
                rrSld.name = sld
                rrSld.value = ip
                rrSld.type = type
                rrSld.ttl = DEFAULT_TLD
                zone.resourceRecords.add(rrSld)
            }
        }

        fun removeIPv4(zone: Zone, sld: String) {
            removeIP(zone, sld, RR_A)
        }

        fun removeIPv6(zone: Zone, sld: String) {
            removeIP(zone, sld, RR_AAAA)
        }

        private fun removeIP(zone: Zone, sld: String, type: String) {
            val rr: ResourceRecord? = searchResourceRecord(zone, sld, type)
            if (rr != null) {
                zone.resourceRecords.remove(rr)
            }
        }

        fun searchResourceRecord(zone: Zone, name: String, type: String): ResourceRecord? {
            val rrO: Optional<ResourceRecord> = zone.resourceRecords.stream()
                .filter { rr: org.domainrobot.sdk.models.generated.ResourceRecord -> rr.type == type && rr.name == name }
                .findFirst()
            return if (rrO.isPresent) rrO.get() else null
        }
    }
}