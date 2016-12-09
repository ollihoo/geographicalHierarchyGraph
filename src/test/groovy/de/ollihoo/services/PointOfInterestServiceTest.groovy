package de.ollihoo.services

import de.ollihoo.domain.AdministrativeUnit
import de.ollihoo.domain.City
import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.PointOfInterestRepository
import de.ollihoo.tourpedia.AttractionDataServiceTestBase

class PointOfInterestServiceTest extends AttractionDataServiceTestBase {
  private static final Long ANY_POI_ID = 12345L
  private static final String ANY_POI_NAME = "POI NAME"
  private static final String ANY_REFERENCE_ID = "foobar#111"
  private static final AdministrativeUnit ANY_LOCATION = new City()
  private static final BigDecimal ANY_LNG = new BigDecimal("22")
  private static final BigDecimal ANY_LAT = new BigDecimal("33")
  private static final String ANY_TYPE = "fooType"
  public static final long EXISTING_POI_ID = 77777L


  private PointOfInterestService pointOfInterestService
  private PointOfInterestRepository pointOfInterestRepository

  def setup() {
    pointOfInterestRepository = Mock(PointOfInterestRepository)
    pointOfInterestService = new PointOfInterestService(pointOfInterestRepository: pointOfInterestRepository)
  }

  def "When POI already exists, it is updated"() {
    PointOfInterest foundPOI = createFakePointOfInterest()

    when:
    pointOfInterestService.insertOrUpdatePoi(foundPOI)

    then:
    1 * pointOfInterestRepository.findByName(_) >> foundPOI
    1 * pointOfInterestRepository.save(foundPOI, 1)
  }

  def "When POI already exists, it's values are updated"() {
    PointOfInterest updatedPoi = new PointOfInterest(id: EXISTING_POI_ID)
    pointOfInterestRepository.findByName(_) >> updatedPoi

    when:
    pointOfInterestService.insertOrUpdatePoi(createFakePointOfInterest())

    then:
    updatedPoi.name == ANY_POI_NAME
    updatedPoi.id == EXISTING_POI_ID
    updatedPoi.referenceId == ANY_REFERENCE_ID
    updatedPoi.location == ANY_LOCATION
    updatedPoi.lat == ANY_LAT
    updatedPoi.lng ==ANY_LNG
    updatedPoi.type == ANY_TYPE
  }

  private PointOfInterest createFakePointOfInterest() {
    new PointOfInterest(
        id: ANY_POI_ID,
        name: ANY_POI_NAME,
        type: ANY_TYPE,
        lat: ANY_LAT,
        lng: ANY_LNG,
        location: ANY_LOCATION,
        referenceId: ANY_REFERENCE_ID
    )
  }

}
