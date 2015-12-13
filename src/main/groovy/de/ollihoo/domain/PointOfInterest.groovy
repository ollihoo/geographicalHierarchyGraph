package de.ollihoo.domain

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

@NodeEntity
class PointOfInterest {
    @GraphId Long id
    String name
    String type
    BigDecimal lat
    BigDecimal lng

    @Relationship(type = "IS_LOCATED_IN", direction = Relationship.INCOMING)
    City city
}
