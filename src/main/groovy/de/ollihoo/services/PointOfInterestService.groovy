package de.ollihoo.services

import de.ollihoo.domain.AdministrativeUnit
import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.PointOfInterestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PointOfInterestService {

  public static final String TOURPEDIA_PREFIX = "tourpedia#"

  @Autowired
  PointOfInterestRepository pointOfInterestRepository

  PointOfInterest insertOrUpdatePoi(PointOfInterest pointOfInterest) {
    PointOfInterest poi = pointOfInterestRepository.findByName(pointOfInterest.name)
    if (poi) {
      poi.name = pointOfInterest.name
      poi.type = pointOfInterest.type
      poi.lat = pointOfInterest.lat
      poi.lng = pointOfInterest.lng
      poi.location = pointOfInterest.location
      poi.referenceId = pointOfInterest.referenceId
      pointOfInterestRepository.save(poi, 1)
    } else {
      pointOfInterestRepository.save(pointOfInterest, 1)
    }
  }

}
