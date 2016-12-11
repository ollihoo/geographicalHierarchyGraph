package de.ollihoo.domain

import groovy.transform.ToString
import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship
import org.neo4j.ogm.annotation.Transient

@NodeEntity @ToString
class City extends AdministrativeUnit {
    @GraphId Long id
    String name
}
