package de.ollihoo.controller

import de.ollihoo.osm.ListingService
import org.springframework.ui.Model
import spock.lang.Specification
import spock.lang.Unroll


class InsertionControllerTest extends Specification {

    private InsertionController controller
    private ListingService listingService



    def setup() {
        listingService = Mock(ListingService)
        controller = new InsertionController(listingService: listingService)
    }

    @Unroll
    def "run createCity for '#city'" () {
        Model model = Mock(Model)

        when:
        def result = controller.index(city, model)

        then:
        1 * listingService.parsePointOfInterestsWithCoordinates("berlin")
        result == "insert"

        where:
        city << [ 'berlin', 'Berlin', 'BERLIN']
    }

}
