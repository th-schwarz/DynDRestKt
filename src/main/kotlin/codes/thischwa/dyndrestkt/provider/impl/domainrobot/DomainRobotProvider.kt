package codes.thischwa.dyndrestkt.provider.impl.domainrobot

import codes.thischwa.dyndrestkt.config.AppConfig
import codes.thischwa.dyndrestkt.model.IpSetting
import codes.thischwa.dyndrestkt.provider.Provider

class DomainRobotProvider(val appConfig: AppConfig, val domainRobotConfigurator: DomainRobotConfigurator, val zcw: ZoneClientWrapper): Provider {
    override fun validateHostConfiguration() {
        TODO("Not yet implemented")
    }

    override fun getConfiguredHosts(): Set<String> {
        TODO("Not yet implemented")
    }

    override fun update(host: String, ipSetting: IpSetting) {
        TODO("Not yet implemented")
    }

    override fun getApitoken(host: String): String {
        TODO("Not yet implemented")
    }
}