package de.ollihoo.controller

import de.ollihoo.domain.LinkedPoi
import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.PointOfInterestRepository
import de.ollihoo.services.RoundTripService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AttractionRoundTripController {

    @Autowired
    private RoundTripService roundTripService

    @Autowired
    PointOfInterestRepository pointOfInterestRepository

    @RequestMapping(value = "/roundtrip", method = RequestMethod.GET)
    LinkedPoi index(@RequestParam(value = "lat", required = false) String lat,
                    @RequestParam(value = "lng", required = false) String lng,
                    Model model) {
        RoundtripCommand command = new RoundtripCommand(lat: lat, lng: lng)
        PointOfInterest personalStartPoint = (command)?
                new PointOfInterest(name: "YOUR START POINT", lat: command.latitude, lng: command.longitude):
                new PointOfInterest(name: "YOUR START POINT", lat: 52.541813, lng: 13.354431)
        def pointOfInterests = pointOfInterestRepository.getPoisInCityOfType("Berlin", "attraction")
        roundTripService.getRoundTripRoute(personalStartPoint, pointOfInterests)
    }


}
