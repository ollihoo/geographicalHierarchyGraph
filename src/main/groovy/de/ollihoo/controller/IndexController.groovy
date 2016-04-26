package de.ollihoo.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class IndexController {

    @RequestMapping(value="/", method = RequestMethod.GET)
    String index() {
        "index"
    }
}
