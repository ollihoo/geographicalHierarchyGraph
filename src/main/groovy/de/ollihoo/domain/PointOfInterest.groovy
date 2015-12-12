package de.ollihoo.domain

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
class PointOfInterest {
    @GraphId Long id
    String name
    String type
    BigDecimal lat
    BigDecimal lng
}
