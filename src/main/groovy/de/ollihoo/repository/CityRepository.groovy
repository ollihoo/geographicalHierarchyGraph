package de.ollihoo.repository

import de.ollihoo.domain.City
import org.springframework.data.neo4j.repository.GraphRepository

public interface CityRepository extends GraphRepository<City> {
    City findByName(String name);
}
