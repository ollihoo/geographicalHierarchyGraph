package de.ollihoo.repository

import de.ollihoo.domain.District
import org.springframework.data.neo4j.repository.GraphRepository

public interface DistrictRepository extends GraphRepository<District> {
    District findByName(String name);
}
