package de.ollihoo.domain

import groovy.transform.ToString
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

@NodeEntity
@ToString
class Address extends AdministrativeUnit {
    @GraphId Long id
    String zip
    String street

    @Relationship(type = "IN", direction = Relationship.OUTGOING)
    AdministrativeUnit location
}
