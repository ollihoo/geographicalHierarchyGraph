package de.ollihoo.repository;

import de.ollihoo.domain.PoiType;
import de.ollihoo.domain.PointOfInterest;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;
import java.util.Set;

public interface PointOfInterestRepository extends GraphRepository<PointOfInterest> {
    PointOfInterest findByName(String name);

    @Query("match (p:PointOfInterest) return p.type as type, count(*) as numberOfEntries order by numberOfEntries desc")
    Set<PoiType> getAllPoiTypes();

    @Query("match (p:PointOfInterest)-[*1..]->(c:City) where c.name={0} and p.type = {1} return p")
    List<PointOfInterest> getPoisInCityOfType(String city, String type);

}