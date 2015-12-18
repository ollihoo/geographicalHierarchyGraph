package de.ollihoo.domain

import org.neo4j.cypher.InvalidArgumentException


class Coordinate {
    private BigDecimal RADIUS = new BigDecimal("6371")
    BigDecimal latitude
    BigDecimal longitude


    def getDistanceInKm(Coordinate anotherCoordinate) {
        if (latitude == null || longitude == null) {
            throw new InvalidCoordinateException("Coordinate contains null values. This is not allowed here")
        }
        if (anotherCoordinate.latitude == null || anotherCoordinate.longitude == null) {
            throw new InvalidCoordinateException("Given coordinate contains null values. This is not allowed here")
        }
        def dLat = deg2rad(anotherCoordinate.latitude - latitude)  // deg2rad below
        def dLon = deg2rad(anotherCoordinate.longitude - longitude)

        def a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(anotherCoordinate.latitude)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)

        def c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        def d = RADIUS * c // Distance in km
        return d
    }

    private BigDecimal deg2rad(BigDecimal deg) {
        deg.multiply((Math.PI / 180))
    }
}
