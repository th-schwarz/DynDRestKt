package codes.thischwa.ddautokt.service

import codes.thischwa.ddautokt.AutoDnsConfig
import codes.thischwa.ddautokt.DDAutoConfig
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ZoneSdk {

    private val log = KotlinLogging.logger {}

    @Autowired
    lateinit var autoDnsconfig : AutoDnsConfig

    @Autowired
    lateinit var config: DDAutoConfig


}