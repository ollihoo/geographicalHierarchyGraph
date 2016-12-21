package de.ollihoo.controller

import de.ollihoo.osm.ListingService
import de.ollihoo.tourpedia.AttractionDataService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class InsertionController {
    private static Logger LOG = LoggerFactory.getLogger(InsertionController)

    @Autowired
    AttractionDataService attractionDataService

    @Autowired
    ListingService listingService

    @RequestMapping(value = "/resolve/{city}", method = RequestMethod.GET)
    def resolve(@PathVariable(value="city") String city, Model model) {
        model.addAttribute("pois", listingService.parseRawPointOfInterests(city))
        "insert"
    }

    @RequestMapping(value = "/create/{city}", method = RequestMethod.GET)
    def index(@PathVariable(value="city") String city, Model model) {
        String normalizedCity = city.toLowerCase()
        try {
            if (normalizedCity == "amsterdam") {
                model.addAttribute("pois", attractionDataService.getAttractionsFor("Amsterdam"))
            } else {
                model.addAttribute("pois", listingService.parsePointOfInterestsWithCoordinates(normalizedCity))
            }
        } catch (all) {
            LOG.error("Insertion failed: " + all.getMessage())
        }
        "insert"
    }
}
