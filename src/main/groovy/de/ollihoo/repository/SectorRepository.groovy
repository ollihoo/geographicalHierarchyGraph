package de.ollihoo.repository

import de.ollihoo.domain.Sector
import org.springframework.data.neo4j.repository.GraphRepository

public interface SectorRepository extends GraphRepository<Sector> {
    Sector findByName(String name);
}
