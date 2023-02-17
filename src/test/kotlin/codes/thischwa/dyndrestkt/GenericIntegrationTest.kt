package codes.thischwa.dyndrestkt

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.net.URI
import java.net.URISyntaxException


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
abstract class GenericIntegrationTest {

    @Value("\${local.server.port}")
    private val port: String = ""

    @Autowired
    var restTemplate: TestRestTemplate? = null

    val baseUrl: String
        get() = "http://localhost:$port/"

    @get:Throws(URISyntaxException::class)
    val baseUri: URI
        get() = URI(baseUrl)
}