package de.ollihoo.domain

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
class City {
    @GraphId Long id
    String name
    String district // Ortsteil
    String sector // Bezirk
    String zip
    String street
    String housenumber
}
