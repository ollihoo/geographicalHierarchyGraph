package de.ollihoo.osm

import de.ollihoo.domain.City
import de.ollihoo.domain.PointOfInterest
import groovy.json.JsonSlurper
import spock.lang.Specification
import spock.lang.Unroll

class OsmServiceSpec extends Specification {

    public static
    final String COMBINED_NAME = "Australische Botschaft, Wallstraße, Scheunenviertel, Mitte, Berlin, 10179, Deutschland"
    OsmService service

    def setup() {
        service = new OsmService()
        service.osmNameResolver = Mock(OsmNameResolver)
    }

    def "request is answered by a point of interest" () {
        given:
        def expectedCity = new City()

        when:
        PointOfInterest result = service.getPointOfInterestByRequest("Berlin, Australische Botschaft")

        then:
        1 * service.osmNameResolver.parsePoiName(COMBINED_NAME) >> "Australische Botschaft"
        1 * service.osmNameResolver.parseCity(COMBINED_NAME) >> expectedCity
        result.type == "embassy"
        result.name == "Australische Botschaft"
        result.lat == new BigDecimal("52.5122655")
        result.lng == new BigDecimal("13.4093648")
        result.city == expectedCity
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
