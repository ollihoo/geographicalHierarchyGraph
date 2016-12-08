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

  PointOfInterest insertOrUpdatePoi(entry, AdministrativeUnit location) {
    PointOfInterest poi = pointOfInterestRepository.findByName(entry.name)
    if (poi) {
      poi.name = entry.name
      poi.type = entry.category
      poi.lat = entry.lat
      poi.lng = entry.lng
      poi.location = location
      poi.referenceId = TOURPEDIA_PREFIX + entry.id + ""
      pointOfInterestRepository.save(poi, 1)
    } else {
      pointOfInterestRepository.save(
          new PointOfInterest(
              name: entry.name,
              type: entry.category,
              lat: entry.lat, lng: entry.lng,
              location: location,
              referenceId: TOURPEDIA_PREFIX + entry.id
          ), 1)
    }
  }

}
