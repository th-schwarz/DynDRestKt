package codes.thischwa.ddautokt

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingController {

    @GetMapping(path = ["/hello"], produces = [MediaType.TEXT_HTML_VALUE])
    fun hello() = "Hello"
}