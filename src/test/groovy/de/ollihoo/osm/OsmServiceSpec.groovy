package de.ollihoo.osm

import de.ollihoo.domain.PointOfInterest
import groovy.json.JsonSlurper
import spock.lang.Specification
import spock.lang.Unroll

class OsmServiceSpec extends Specification {

    static OsmService service = new OsmService()

    def "request is answered by a point of interest" () {
        when:
        PointOfInterest result = service.getPointOfInterestByRequest("Berlin, Australische Botschaft")

        then:
        result.type == "embassy"
        result.name == "Australische Botschaft, Wallstraße, Scheunenviertel, Mitte, Berlin, 10179, Deutschland"
        result.lat == new BigDecimal("52.5122655")
        result.lng == new BigDecimal("13.4093648")
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
