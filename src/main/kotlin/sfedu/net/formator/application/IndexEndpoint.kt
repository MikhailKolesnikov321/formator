package sfedu.net.formator.application

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.view.RedirectView

@Controller
class IndexEndpoint {
    @GetMapping("/")
    operator fun invoke() = RedirectView("/swagger-ui/index.html")
}
