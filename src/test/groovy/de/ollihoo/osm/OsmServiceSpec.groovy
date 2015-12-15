package de.ollihoo.osm

import de.ollihoo.domain.City
import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.PointOfInterestRepository
import spock.lang.Specification
import spock.lang.Unroll

class OsmServiceSpec extends Specification {

    public static
    final String COMBINED_NAME = "Australische Botschaft, Wallstraße, Scheunenviertel, Mitte, Berlin, 10179, Deutschland"
    OsmService service

    def setup() {
        service = new OsmService()
        service.osmNameResolver = Mock(OsmNameResolver)
        service.pointOfInterestRepository = Mock(PointOfInterestRepository)
    }

    def "request is answered by a point of interest" () {
        given:
        def expectedCity = new City()

        when:
        PointOfInterest result = service.getPointOfInterestByRequest("Berlin, Australische Botschaft")

        then:
        1 * service.osmNameResolver.parsePoiName(_) >> "Australische Botschaft"
        1 * service.osmNameResolver.parseCombinedAddress(_) >> expectedCity
        1 * service.pointOfInterestRepository.save(_, 1) >> { it[0] }
        result.type == "embassy"
        result.name == "Australische Botschaft"
        result.lat == new BigDecimal("52.5122655")
        result.lng == new BigDecimal("13.4093648")
        result.location == expectedCity
    }

    def "request with coordinates is saved to graph db" () {
        given:
        def expectedCity = new City()

        when:
        service.getPointOfInterestByRequest("Berlin, Australische Botschaft")

        then:
        1 * service.osmNameResolver.parsePoiName(_) >> "Australische Botschaft"
        1 * service.osmNameResolver.parseCombinedAddress(_) >> expectedCity
        1 * service.pointOfInterestRepository.save(_, 1)
    }


    @Unroll
    def "Request #input is answered by null" () {
        when:
        def result = service.getPointOfInterestByRequest(input)

        then:
        result == null

        where:
        input << [null, ""]
    }
}
