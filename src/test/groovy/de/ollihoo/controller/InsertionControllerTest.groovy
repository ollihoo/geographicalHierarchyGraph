package de.ollihoo.controller

import de.ollihoo.osm.ListingService
import de.ollihoo.tourpedia.AttractionDataService
import org.slf4j.Logger
import org.springframework.ui.Model
import spock.lang.Specification
import spock.lang.Unroll


class InsertionControllerTest extends Specification {

    private InsertionController controller
    private ListingService listingService
    private AttractionDataService attractionDataService
    private Logger logger

    private Model model

    def setup() {
        attractionDataService = Mock(AttractionDataService)
        listingService = Mock(ListingService)
        logger = Mock(Logger)

        controller = new InsertionController(
                attractionDataService: attractionDataService,
                listingService: listingService,
                logger: logger
        )

        model = Mock(Model)
    }

    @Unroll
    def "run createCity for prepared data of '#city'" () {
        when:
        def result = controller.index(city, model)

        then:
        1 * listingService.parsePointOfInterestsWithCoordinates("berlin")
        result == "insert"

        where:
        city << [ 'berlin', 'Berlin', 'BERLIN']
    }

    @Unroll
    def "run createCity for city '#city'" () {
        when:
        def result = controller.index(city, model)

        then:
        1 * attractionDataService.getAttractionsFor("Amsterdam")
        result == "insert"

        where:
        city << [ 'Amsterdam', 'amsterdam', 'AMSTERdam']
    }

    def "When listingService throws any exception, it is logged" () {

        when:
        controller.index("berlin", model)

        then:
        listingService.parsePointOfInterestsWithCoordinates(_) >> { throw new Exception() }
        1 * logger.error(_)
    }

    def "When attractionDataService throws any exception, it is logged" () {

        when:
        controller.index("amsterdam", model)

        then:
        attractionDataService.getAttractionsFor(_) >> { throw new Exception() }
        1 * logger.error(_)
    }

}
