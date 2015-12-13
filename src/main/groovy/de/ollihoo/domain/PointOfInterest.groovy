package de.ollihoo.domain

import groovy.transform.ToString
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

@NodeEntity @ToString
class PointOfInterest {
    @GraphId Long id
    String name
    String type
    BigDecimal lat
    BigDecimal lng

    @Relationship(type = "LOCATED_AT", direction = Relationship.OUTGOING)
    AdministrativeUnit location

}
