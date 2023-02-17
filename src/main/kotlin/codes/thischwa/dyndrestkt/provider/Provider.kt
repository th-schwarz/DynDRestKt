package codes.thischwa.dyndrestkt.provider

import codes.thischwa.dyndrestkt.model.IpSetting
import codes.thischwa.dyndrestkt.util.NetUtil
import mu.KLogger
import mu.KotlinLogging
import java.io.IOException

interface Provider {

    /**
     * Validates the host configuration.
     */
    @Throws(IllegalArgumentException::class)
    fun validateHostConfiguration()

    /**
     * Returns all hosts that are configured correctly.
     *
     * @return all hosts that are configured correctly
     */
    fun getConfiguredHosts(): Set<String>

    /**
     * Checks if the desired 'hosts' exists. <br></br>
     * Hint: There is no need to override it!
     *
     * @param host the host
     * @return the boolean
     */
    fun hostExists(host: String): Boolean {
        return getConfiguredHosts().contains(host)
    }

    /**
     * Update the desired 'host' with the desired IP setting.
     *
     * @param host      the host
     * @param ipSetting the ip setting
     * @throws ProviderException the provider exception
     */
    @Throws(ProviderException::class)
    fun update(host: String, ipSetting: IpSetting)

    /**
     * Gets apitoken of the desired 'host'.
     *
     * @param host the host
     * @return the apitoken
     */
    fun getApitoken(host: String): String

    /**
     * Determine the IPs of the 'host'.
     *
     * @param host The host for which the IPs are to be determined.
     * @return IP setting of the 'host'.
     * @throws ProviderException if the IPs couldn't be determined.
     */
    @Throws(ProviderException::class)
    fun info(host: String): IpSetting {
        return try {
            NetUtil.resolve(host)
        } catch (e: IOException) {
            throw ProviderException(e)
        }
    }
}