package de.ollihoo.controller

import org.springframework.boot.autoconfigure.web.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ErrorPageController implements ErrorController {

    @Override
    @RequestMapping(value = "/error")
    String getErrorPath() {
        "error"
    }
}
