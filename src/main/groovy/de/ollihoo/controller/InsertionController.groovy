package de.ollihoo.controller

import de.ollihoo.osm.ListingService
import de.ollihoo.repository.PointOfInterestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class InsertionController {

    @Autowired
    PointOfInterestRepository pointOfInterestRepository

    @Autowired
    ListingService listingService

    @RequestMapping(value = "/resolve/{city}", method = RequestMethod.GET)
    def resolve(@PathVariable(value="city") String city, Model model) {
        model.addAttribute("pois", listingService.parseRawPointOfInterests(city))
        "insert"
    }

    @RequestMapping(value = "/create/{city}", method = RequestMethod.GET)
    def index(@PathVariable(value="city") String city, Model model) {
        model.addAttribute("pois", listingService.parsePointOfInterestsWithCoordinates(city))
        "insert"
    }
}
