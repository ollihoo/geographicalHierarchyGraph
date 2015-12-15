package de.ollihoo.osm

import de.ollihoo.repository.PointOfInterestRepository
import spock.lang.Specification

class ListingServiceSpec extends Specification {

    ListingService service = new ListingService()

    def setup() {
        service.osmNameResolver = Mock(OsmNameResolver)
        service.pointOfInterestRepository = Mock(PointOfInterestRepository)
    }

    def "ListingService should parse internal poi list with 210 entries" () {
        when:
        List result = service.parsePointOfInterestsWithCoordinates("berlin")

        then:
        result.size() == 210
    }
}
