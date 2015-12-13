package de.ollihoo.repository

import de.ollihoo.domain.PointOfInterest
import org.springframework.data.neo4j.repository.GraphRepository

public interface PointOfInterestRepository extends GraphRepository<PointOfInterest> {

}