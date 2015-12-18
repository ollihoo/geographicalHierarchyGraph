package de.ollihoo.domain

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


class CoordinateSpec extends Specification {
    @Shared
    private Coordinate LEIPZIG_MAIN_STATION = new Coordinate(latitude: 51.34432, longitude: 12.38461)
    @Shared
    private Coordinate LEIPZIG_MONUMENTUM = new Coordinate(latitude: 51.31286, longitude: 12.41766)
    @Shared
    private Coordinate BERLIN_MAIN_STATION = new Coordinate(latitude: 52.52497, longitude: 13.37014)

    @Unroll
    def "happy trail calculation "() {
        when:
        def result = LEIPZIG_MAIN_STATION.getDistanceInKm(input)
        then:
        result == output
        where:
        input                | output
        BERLIN_MAIN_STATION  | 147.64559807132298
        LEIPZIG_MONUMENTUM   | 4.184552916253633
        LEIPZIG_MAIN_STATION | 0.0
    }

    def "edge case: 0,0"() {
        given:
        def c = new Coordinate(latitude: 0, longitude: 0)
        expect:
        BERLIN_MAIN_STATION.getDistanceInKm(c) == 5971.861107621643
    }

    @Unroll
    def "edge case: given coordinate contains null values"() {
        given:
        def c = new Coordinate(latitude: lat, longitude: lng)
        when:
        BERLIN_MAIN_STATION.getDistanceInKm(c)
        then:
        thrown(InvalidCoordinateException)
        where:
        lat  | lng
        53   | null
        null | 12
        null | null
    }

    @Unroll
    def "edge case: set coordinate contains null values"() {
        given:
        def c = new Coordinate(latitude: lat, longitude: lng)
        when:
        c.getDistanceInKm(BERLIN_MAIN_STATION)
        then:
        thrown(InvalidCoordinateException)
        where:
        lat  | lng
        53   | null
        null | 12
        null | null
    }

}
