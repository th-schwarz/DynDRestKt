package codes.thischwa.dyndrestkt.provider.impl.domainrobot

import codes.thischwa.dyndrestkt.model.IpSetting
import codes.thischwa.dyndrestkt.provider.ProviderException
import org.domainrobot.sdk.client.clients.ZoneClient
import org.domainrobot.sdk.models.DomainrobotApiException
import org.domainrobot.sdk.models.generated.ResourceRecord
import org.domainrobot.sdk.models.generated.Zone
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.UnknownHostException

class ZoneClientWrapper(val zc: ZoneClient, val customHeaders: Map<String, String>, val defaultTtl: Long) {

    enum class ResouceRecordTypeIP {
        A, AAAA
    }

    fun searchResourceRecord(zone: Zone, name: String, type: ResouceRecordTypeIP): ResourceRecord? {
        return zone.resourceRecords.stream()
            .filter { rr: ResourceRecord -> (rr.type == type.toString()) && rr.name == name }
            .findFirst().orElse(null)
    }

    fun deriveZone(host: String): String {
        val cnt = host.chars().filter { ch: Int -> ch == '.'.code }.count()
        require(cnt >= 2) { "'host' must be a sub domain." }
        return host.substring(host.indexOf(".") + 1)
    }

    fun hasIPsChanged(zone: Zone, sld: String, ipSetting: IpSetting): Boolean {
        if (ipSetting.isNotSet()) return false
        val rrv4 = searchResourceRecord(zone, sld, ResouceRecordTypeIP.A)
        val rrv6 = searchResourceRecord(zone, sld, ResouceRecordTypeIP.AAAA)
        val ipv4Changed = !hasIPChanged(rrv4, ipSetting.ipv4)
        val ipv6Changed = !hasIPChanged(rrv6, ipSetting.ipv6)
        return ipv4Changed || ipv6Changed
    }

    private fun hasIPChanged(rr: ResourceRecord?, ip: InetAddress?): Boolean {
        return if (rr == null || ip == null) false else try {
            val rrIp = InetAddress.getByName(rr.value)
            rrIp == ip
        } catch (e: UnknownHostException) {
            throw IllegalArgumentException("Couldn't get Ip from value: " + rr.value, e)
        }
    }

    @Throws(ProviderException::class)
    fun update(zone: Zone) {
        try {
            zc.update(zone, customHeaders)
        } catch (e: DomainrobotApiException) {
            throw ProviderException("API exception", e)
        } catch (e: Exception) {
            throw ProviderException("Unknown exception", e)
        }
    }

    /**
     * Processes a zone-info for 'zone' and 'primaryNameServer'.
     *
     * @param zone              the zone to process the info
     * @param primaryNameServer the primary NS of the zone
     * @return the complete zone object from the domainrobot sdk
     *
     * @throws ProviderException if an exception happens while processing the zone-info
     */
    @Throws(ProviderException::class)
    fun info(zone: String?, primaryNameServer: String?): Zone? {
        return try {
            zc.info(zone, primaryNameServer, customHeaders)
        } catch (e: DomainrobotApiException) {
            throw ProviderException("API exception", e)
        } catch (e: Exception) {
            throw ProviderException("Unknown exception", e)
        }
    }

    /**
     * Processes the ip settings for the desired zone and subtld,
     * The corresponding resource record will be updated or removed if null.
     *
     * @param zone      the zone
     * @param sld       the sld
     * @param ipSetting the ip setting
     */
    fun process(zone: Zone, sld: String, ipSetting: IpSetting) {
        processIPv4(zone, sld, ipSetting.ipv4)
        processIPv6(zone, sld, ipSetting.ipv6)
    }

    private fun processIPv4(zone: Zone, sld: String, ip: Inet4Address?) {
        if (ip != null) addOrUpdateIPv4(zone, sld, ip) else removeIPv4(zone, sld)
    }

    private fun processIPv6(zone: Zone, sld: String, ip: Inet6Address?) {
        if (ip != null) addOrUpdateIPv6(zone, sld, ip) else removeIPv6(zone, sld)
    }

    private fun addOrUpdateIPv4(zone: Zone, sld: String, ip: Inet4Address) {
        addOrUpdateIP(zone, sld, ip, ResouceRecordTypeIP.A)
    }

    private fun addOrUpdateIPv6(zone: Zone, sld: String, ip: Inet6Address) {
        addOrUpdateIP(zone, sld, ip, ResouceRecordTypeIP.AAAA)
    }

    private fun addOrUpdateIP(zone: Zone, sld: String, ip: InetAddress, type: ResouceRecordTypeIP) {
        val rr = searchResourceRecord(zone, sld, type)
        if (rr != null) {
            rr.value = ip.hostAddress
            rr.ttl = defaultTtl
        } else {
            val rrSld = ResourceRecord()
            rrSld.name = sld
            rrSld.value = ip.hostAddress
            rrSld.type = type.toString()
            rrSld.ttl = defaultTtl
            zone.resourceRecords.add(rrSld)
        }
    }

    fun removeIPv4(zone: Zone, sld: String) {
        removeIP(zone, sld, ResouceRecordTypeIP.A)
    }

    fun removeIPv6(zone: Zone, sld: String) {
        removeIP(zone, sld, ResouceRecordTypeIP.AAAA)
    }

    fun removeIP(zone: Zone, sld: String, type: ResouceRecordTypeIP) {
        val rr = searchResourceRecord(zone, sld, type)
        if (rr != null) {
            zone.resourceRecords.remove(rr)
            zone.resourceRecords.remove(rr)
        }
    }
}