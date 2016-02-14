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
        def pointOfInterests = pointOfInterestRepository.getPoisInCityOfType("Berlin", "attraction")
        PointOfInterest personalStartPoint = new PointOfInterest(name: "YOUR START POINT", lat:  52.541813, lng: 13.354431)
        def route = roundTripService.getRoundTripRoute(personalStartPoint, pointOfInterests)
        model.addAttribute("route", route)
        "index"
    }


}
