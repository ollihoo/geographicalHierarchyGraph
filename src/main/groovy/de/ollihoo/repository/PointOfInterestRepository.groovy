package de.ollihoo.repository

import de.ollihoo.domain.PoiType
import de.ollihoo.domain.PointOfInterest
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository

public interface PointOfInterestRepository extends GraphRepository<PointOfInterest> {
    PointOfInterest findByName(String name);

    @Query("match (p:PointOfInterest) return p.type as type, count(*) as numberOfEntries order by numberOfEntries desc")
    Set<PoiType> getAllPoiTypes();


}