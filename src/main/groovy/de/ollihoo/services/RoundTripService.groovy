package de.ollihoo.services

import de.ollihoo.domain.Coordinate
import de.ollihoo.domain.PointOfInterest
import org.springframework.stereotype.Service

@Service
class RoundTripService {

    def List<PointOfInterest> getRoundTripRoute(Coordinate currentPosition, List<PointOfInterest> pointOfInterests) {
        if (! currentPosition) {
            return []
        }
        def resultList = []
        def remainingPois = pointOfInterests.collect { it }
        while (! remainingPois.empty) {
            resultList << getNextNearestPoi(currentPosition, remainingPois)
        }
        resultList
    }

    private PointOfInterest getNextNearestPoi(Coordinate currentPosition, List<PointOfInterest> operationalList) {
        def lastPosition = findNearestPoi(currentPosition, operationalList)
        operationalList.remove(lastPosition)
        lastPosition
    }

    private PointOfInterest findNearestPoi(Coordinate currentPosition, List<PointOfInterest> pois) {
        def nearestPoi
        def shortestDistance
        pois.collect { PointOfInterest poi ->
            def dist = currentPosition.getDistanceInKm(poi.coordinate)
            if (!shortestDistance || shortestDistance > dist) {
                nearestPoi = poi
                shortestDistance = dist
            }
        }
        nearestPoi
    }

}
