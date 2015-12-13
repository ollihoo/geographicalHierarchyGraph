package de.ollihoo.domain

import org.springframework.data.neo4j.annotation.QueryResult

@QueryResult
class PoiType {
    String type
    Integer numberOfEntries
}
