package de.ollihoo.domain

import groovy.transform.ToString
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.Transient

@NodeEntity @ToString
class PointOfInterest {
    @GraphId Long id
    String name
    String type
    BigDecimal lat
    BigDecimal lng

    @Transient
    private Coordinate coordinate

    @Relationship(type = "LOCATED_AT", direction = Relationship.OUTGOING)
    AdministrativeUnit location

    Coordinate getCoordinate() {
        if (! coordinate) {
            coordinate = new Coordinate(latitude: lat, longitude: lng)
        }
        coordinate
    }




}
