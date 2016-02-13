package de.ollihoo.services

import de.ollihoo.domain.Coordinate
import de.ollihoo.domain.InvalidCoordinateException
import de.ollihoo.domain.PointOfInterest
import spock.lang.Specification


class RoundTripServiceTest extends Specification {
    private static final Coordinate FRIEDRICHSTRASSE = new Coordinate(latitude: 52.51231, longitude: 13.38865)
    private static final Coordinate ALEXANDERPLATZ = new Coordinate(latitude: 52.5215800, longitude: 13.4162300)
    private static final Coordinate POTSDAMER_PLATZ = new Coordinate(latitude:52.5087317, longitude: 13.3770476)
    private static final Coordinate NEPTUNBRUNNEN = new Coordinate(latitude:52.5195871, longitude: 13.4068573245652)
    private static final Coordinate BRANDENBURGER_TOR = new Coordinate(latitude:52.516275, longitude: 13.377704)

    private static final PointOfInterest POI_POTSDAMER_PLATZ = createPointOfInterest(POTSDAMER_PLATZ)
    private static final PointOfInterest POI_NEPTUNBRUNNEN = createPointOfInterest(NEPTUNBRUNNEN)
    private static final PointOfInterest POI_ALEXANDERPLATZ = createPointOfInterest(ALEXANDERPLATZ)
    private static final PointOfInterest POI_BRANDENBURGER_TOR = createPointOfInterest(BRANDENBURGER_TOR)

    private RoundTripService service

    def setup() {
        service = new RoundTripService()
    }

    def "request with one poi returns a list with this poi" () {
        when:
        def result = service.getRoundTripRoute(FRIEDRICHSTRASSE, [POI_NEPTUNBRUNNEN])
        then:
        result == [ POI_NEPTUNBRUNNEN ]
    }

    def "from friedrichstrasse, pois are sorted potsdamerPlatz, neptunBrunnen" () {
        when:
        def result = service.getRoundTripRoute(FRIEDRICHSTRASSE, [POI_NEPTUNBRUNNEN, POI_POTSDAMER_PLATZ])
        then:
        result == [POI_POTSDAMER_PLATZ, POI_NEPTUNBRUNNEN ]
    }

    def "from alexanderplatz, pois are sorted potsdamerPlatz, neptunBrunnen" () {
        when:
        def result = service.getRoundTripRoute(ALEXANDERPLATZ, [POI_NEPTUNBRUNNEN, POI_POTSDAMER_PLATZ])
        then:
        result == [POI_NEPTUNBRUNNEN, POI_POTSDAMER_PLATZ ]
    }

    def "from friedrichstrasse, sorted pois list is brandenburgerTor, potsdamerPlatz, neptunBrunnen, alexanderPlatz" () {
        when:
        def result = service.getRoundTripRoute(FRIEDRICHSTRASSE, [POI_NEPTUNBRUNNEN, POI_ALEXANDERPLATZ, POI_POTSDAMER_PLATZ, POI_BRANDENBURGER_TOR])
        then:
        result == [POI_BRANDENBURGER_TOR, POI_POTSDAMER_PLATZ, POI_NEPTUNBRUNNEN, POI_ALEXANDERPLATZ ]
    }

    def "when one coordinate is invalid an InvalidCoordinateException is thrown" () {
        when:
        service.getRoundTripRoute(FRIEDRICHSTRASSE, [createPointOfInterest(new Coordinate(latitude:  52.333, longitude: null))])
        then:
        thrown(InvalidCoordinateException)
    }

    def "empty list of pois returns empty list" () {
        when:
        def result = service.getRoundTripRoute(FRIEDRICHSTRASSE, [])
        then:
        result == []
    }

    def "null list of pois returns empty list" () {
        when:
        def result = service.getRoundTripRoute(FRIEDRICHSTRASSE, null)

        then:
        result == []
    }

    def "null coordinate returns empty list" () {
        when:
        def result = service.getRoundTripRoute(null, [createPointOfInterest(ALEXANDERPLATZ)])

        then:
        result == []
    }

    private static createPointOfInterest(Coordinate coordinate) {
        new PointOfInterest(lat: coordinate.latitude, lng: coordinate.longitude)
    }

}
