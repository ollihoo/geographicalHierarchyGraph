package de.ollihoo.osm

import de.ollihoo.repository.PointOfInterestRepository
import spock.lang.Specification

/**
 * Created by olli on 13.12.2015.
 */
class ListingServiceSpec extends Specification {

    ListingService service = new ListingService()

    def setup() {
        service.osmNameResolver = Mock(OsmNameResolver)
        service.pointOfInterestRepository = Mock(PointOfInterestRepository)
    }

    def "ListingService should parse internal poi list with 210 entries" () {
        when:
        List result = service.parsePointOfInterests()

        then:
        result.size() == 210
    }
}
