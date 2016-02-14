package de.ollihoo.services

import de.ollihoo.domain.Coordinate
import de.ollihoo.domain.InvalidCoordinateException
import de.ollihoo.domain.LinkedPoi
import de.ollihoo.domain.PointOfInterest
import org.springframework.stereotype.Service

@Service
class RoundTripService {

    def LinkedPoi getRoundTripRoute(PointOfInterest currentPosition, List<PointOfInterest> pointOfInterests) {
        if (! currentPosition) {
            throw new InvalidCoordinateException("currentPosition is invalid")
        }
        LinkedPoi startPoi = new LinkedPoi(currentPosition)
        def remainingPois = pointOfInterests.collect { new LinkedPoi(it) }
        LinkedPoi currentPoi = startPoi
        while (! remainingPois.empty) {
            def nextPoi = getNextNearestPoi(currentPosition.coordinate, remainingPois)
            currentPoi.nextPoi = nextPoi
            currentPoi = nextPoi
        }
        startPoi
    }

    private LinkedPoi getNextNearestPoi(Coordinate currentPosition, List<LinkedPoi> operationalList) {
        def lastPosition = findNearestPoi(currentPosition, operationalList)
        operationalList.remove(lastPosition)
        lastPosition
    }

    private LinkedPoi findNearestPoi(Coordinate currentPosition, List<LinkedPoi> pois) {
        def nearestPoi
        def shortestDistance
        pois.collect { LinkedPoi poi ->
            def dist = currentPosition.getDistanceInKm(poi.poi.coordinate)
            if (!shortestDistance || shortestDistance > dist) {
                nearestPoi = poi
                shortestDistance = dist
            }
        }
        nearestPoi
    }

}
