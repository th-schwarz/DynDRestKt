package codes.thischwa.dyndrestkt.provider.impl.domainrobot

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

@ConditionalOnProperty(name = ["dyndrest.provider"], havingValue = "domainrobot")
class Zone {
    lateinit var name: String
    lateinit var ns: String
    lateinit var hosts: MutableList<String>
}