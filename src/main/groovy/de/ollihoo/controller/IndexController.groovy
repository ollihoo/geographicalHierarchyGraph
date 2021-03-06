package de.ollihoo.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class IndexController {

    @Value('${app.version}')
    private String version

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String index(Model model) {
        model.addAttribute("version", version)
        "index"
    }
}
