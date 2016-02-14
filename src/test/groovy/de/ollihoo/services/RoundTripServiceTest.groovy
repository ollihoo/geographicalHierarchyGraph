package de.ollihoo.services

import de.ollihoo.domain.Coordinate
import de.ollihoo.domain.InvalidCoordinateException
import de.ollihoo.domain.LinkedPoi
import de.ollihoo.domain.PointOfInterest
import spock.lang.Specification


class RoundTripServiceTest extends Specification {
    private static final Coordinate FRIEDRICHSTRASSE = new Coordinate(latitude: 52.51231, longitude: 13.38865)
    private static final Coordinate ALEXANDERPLATZ = new Coordinate(latitude: 52.5215800, longitude: 13.4162300)
    private static final Coordinate POTSDAMER_PLATZ = new Coordinate(latitude:52.5087317, longitude: 13.3770476)
    private static final Coordinate NEPTUNBRUNNEN = new Coordinate(latitude:52.5195871, longitude: 13.4068573245652)
    private static final Coordinate BRANDENBURGER_TOR = new Coordinate(latitude:52.516275, longitude: 13.377704)

    private static final PointOfInterest MY_HOTEL_IN_FRIEDRICHSTR = createPointOfInterest(FRIEDRICHSTRASSE)
    private static final PointOfInterest MY_HOTEL_AT_ALEX = createPointOfInterest(ALEXANDERPLATZ)
    private static final PointOfInterest POI_POTSDAMER_PLATZ = createPointOfInterest(POTSDAMER_PLATZ)
    private static final PointOfInterest POI_NEPTUNBRUNNEN = createPointOfInterest(NEPTUNBRUNNEN)
    private static final PointOfInterest POI_ALEXANDERPLATZ = createPointOfInterest(ALEXANDERPLATZ)
    private static final PointOfInterest POI_BRANDENBURGER_TOR = createPointOfInterest(BRANDENBURGER_TOR)

    private RoundTripService service

    def setup() {
        service = new RoundTripService()
    }

    def "empty list of pois returns start position as first poi" () {
        when:
        LinkedPoi result = service.getRoundTripRoute(MY_HOTEL_IN_FRIEDRICHSTR, [])
        then:
        result.poi == MY_HOTEL_IN_FRIEDRICHSTR
    }

    def "empty list of pois returns Poi without next poi" () {
        when:
        LinkedPoi result = service.getRoundTripRoute(MY_HOTEL_IN_FRIEDRICHSTR, [])
        then:
        result.nextPoi == null
    }

    def "request with one poi returns this poi as next poi" () {
        when:
        LinkedPoi result = service.getRoundTripRoute(MY_HOTEL_IN_FRIEDRICHSTR, [POI_NEPTUNBRUNNEN])
        then:
        result.nextPoi.poi == POI_NEPTUNBRUNNEN
    }

    def "from friedrichstrasse, pois are sorted potsdamerPlatz, neptunBrunnen" () {
        when:
        def result = service.getRoundTripRoute(MY_HOTEL_IN_FRIEDRICHSTR, [POI_NEPTUNBRUNNEN, POI_POTSDAMER_PLATZ])
        then:
        result.nextPoi.poi == POI_POTSDAMER_PLATZ
        result.nextPoi.nextPoi.poi == POI_NEPTUNBRUNNEN
    }

    def "from alexanderplatz, pois are sorted potsdamerPlatz, neptunBrunnen" () {
        when:
        def result = service.getRoundTripRoute(MY_HOTEL_AT_ALEX, [POI_NEPTUNBRUNNEN, POI_POTSDAMER_PLATZ])
        then:
        result.nextPoi.poi == POI_NEPTUNBRUNNEN
        result.nextPoi.nextPoi.poi == POI_POTSDAMER_PLATZ
    }

    def "from friedrichstrasse, sorted pois list is brandenburgerTor, potsdamerPlatz, neptunBrunnen, alexanderPlatz" () {
        when:
        def result = service.getRoundTripRoute(MY_HOTEL_IN_FRIEDRICHSTR, [POI_NEPTUNBRUNNEN, POI_ALEXANDERPLATZ, POI_POTSDAMER_PLATZ, POI_BRANDENBURGER_TOR])
        then:
        result.nextPoi.poi == POI_BRANDENBURGER_TOR
        result.nextPoi.nextPoi.poi == POI_POTSDAMER_PLATZ
        result.nextPoi.nextPoi.nextPoi.poi == POI_NEPTUNBRUNNEN
        result.nextPoi.nextPoi.nextPoi.nextPoi.poi == POI_ALEXANDERPLATZ
    }

    def "when one coordinate is invalid an InvalidCoordinateException is thrown" () {
        when:
        service.getRoundTripRoute(MY_HOTEL_IN_FRIEDRICHSTR, [createPointOfInterest(new Coordinate(latitude:  52.333, longitude: null))])
        then:
        thrown(InvalidCoordinateException)
    }

    def "null list of pois returns empty list" () {
        when:
        def result = service.getRoundTripRoute(MY_HOTEL_IN_FRIEDRICHSTR, null)

        then:
        result.nextPoi == null
    }

    def "null coordinate throws InvalidCoordinateException" () {
        when:
        service.getRoundTripRoute(null, [createPointOfInterest(ALEXANDERPLATZ)])

        then:
        thrown(InvalidCoordinateException)
    }

    private static createPointOfInterest(Coordinate coordinate) {
        new PointOfInterest(lat: coordinate.latitude, lng: coordinate.longitude)
    }

}
