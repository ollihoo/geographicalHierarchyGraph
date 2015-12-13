package de.ollihoo.domain

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

@NodeEntity
class Sector extends AdministrativeUnit {
    @GraphId Long id
    String name

    @Relationship(type = "IN", direction = Relationship.OUTGOING)
    City city
}
