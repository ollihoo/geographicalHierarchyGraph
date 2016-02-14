package de.ollihoo.controller

import de.ollihoo.domain.Coordinate
import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.PointOfInterestRepository
import de.ollihoo.services.RoundTripService
import org.springframework.beans.factory.annotation.Autowire
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class AttractionRoundTripController {

    @Autowired
    private RoundTripService roundTripService

    @Autowired
    PointOfInterestRepository pointOfInterestRepository

    @RequestMapping(value="/", method=RequestMethod.GET)
    String index(Model model) {
        def pointOfInterests = pointOfInterestRepository.getPoisInCityOfType("Hamburg", "attraction")
        def currentPosition = new Coordinate(latitude: 52.541813, longitude:  13.354431)
        def route = roundTripService.getRoundTripRoute(currentPosition, pointOfInterests)
        model.addAttribute("route", route)
        "index"
    }


}
