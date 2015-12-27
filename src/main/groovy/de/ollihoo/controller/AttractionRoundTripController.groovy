package de.ollihoo.controller

import de.ollihoo.repository.PointOfInterestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class AttractionRoundTripController {

    @Autowired
    private PointOfInterestRepository pointOfInterestRepository

    @RequestMapping(value="/", method=RequestMethod.GET)
    String index(Model model) {
        List pois = pointOfInterestRepository.getPoisInCityOfType("Hamburg", "attraction")
        model.addAttribute("pois", pois)
        "index"
    }
}
