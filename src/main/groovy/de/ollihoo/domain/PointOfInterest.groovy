package de.ollihoo.domain

import groovy.transform.ToString
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.Transient

@NodeEntity @ToString
class PointOfInterest extends AdministrativeUnit {
    @GraphId Long id
    String name
    String type
    String referenceId
    
    @Relationship(type = "LOCATED_AT", direction = Relationship.OUTGOING)
    AdministrativeUnit location

}
