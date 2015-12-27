package de.ollihoo.controller

import de.ollihoo.repository.PointOfInterestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class TypeController {

    @Autowired
    PointOfInterestRepository pointOfInterestRepository

    @RequestMapping(value = "/types", method = RequestMethod.GET)
    def index(Model model) {
        model.addAttribute("poiTypes", pointOfInterestRepository.allPoiTypes)
        "type"
    }
}
