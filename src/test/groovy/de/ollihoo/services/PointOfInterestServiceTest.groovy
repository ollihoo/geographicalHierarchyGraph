package de.ollihoo.services

import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.PointOfInterestRepository
import de.ollihoo.tourpedia.AttractionDataServiceTestBase

class PointOfInterestServiceTest extends AttractionDataServiceTestBase {
  public static final Long ANY_POI_ID = 12345L

  private PointOfInterestService pointOfInterestService
  private PointOfInterestRepository pointOfInterestRepository

  def setup() {
    pointOfInterestRepository = Mock(PointOfInterestRepository)
    pointOfInterestService = new PointOfInterestService(pointOfInterestRepository: pointOfInterestRepository)
  }

  def "When POI already exists, it is updated" () {
    PointOfInterest foundPoi = createFakePointOfInterest()

    when:
    pointOfInterestService.insertOrUpdatePoi(TOURPEDIA_ENTRY_WITHOUT_ADDRESS, AMSTERDAM)

    then:
    1 * pointOfInterestRepository.findByName(TOURPEDIA_ENTRY_WITHOUT_ADDRESS.name) >> foundPoi
    1 * pointOfInterestRepository.save(foundPoi, 1)
    foundPoi.id == ANY_POI_ID
    foundPoi.name == TOURPEDIA_ENTRY_WITHOUT_ADDRESS.name
    foundPoi.type == TOURPEDIA_ENTRY_WITHOUT_ADDRESS.category
    foundPoi.lat == TOURPEDIA_ENTRY_WITHOUT_ADDRESS.lat
    foundPoi.lng == TOURPEDIA_ENTRY_WITHOUT_ADDRESS.lng
    foundPoi.location == AMSTERDAM
  }

  def "When POI without address already exists, it is updated" () {
    PointOfInterest foundPoi = createFakePointOfInterest()

    when:
    pointOfInterestService.insertOrUpdatePoi(TOURPEDIA_ENTRY_WITHOUT_ADDRESS, AMSTERDAM)

    then:
    1 * pointOfInterestRepository.findByName(TOURPEDIA_ENTRY_WITHOUT_ADDRESS.name) >> foundPoi
    1 * pointOfInterestRepository.save(foundPoi, 1)
    foundPoi.id == ANY_POI_ID
    foundPoi.name == TOURPEDIA_ENTRY_WITHOUT_ADDRESS.name
    foundPoi.type == TOURPEDIA_ENTRY_WITHOUT_ADDRESS.category
    foundPoi.lat == TOURPEDIA_ENTRY_WITHOUT_ADDRESS.lat
    foundPoi.lng == TOURPEDIA_ENTRY_WITHOUT_ADDRESS.lng
    foundPoi.location == AMSTERDAM
    foundPoi.referenceId == "tourpedia#" + TOURPEDIA_ENTRY_WITHOUT_ADDRESS.id
  }
  def "When POI with address already exists, it is updated" () {
    PointOfInterest foundPoi = createFakePointOfInterest()

    when:
    def actualPoi = pointOfInterestService.insertOrUpdatePoi(TOURPEDIA_ENTRY_WITH_ADDRESS, AMSTERDAM)

    then:
    1 * pointOfInterestRepository.findByName(TOURPEDIA_ENTRY_WITH_ADDRESS.name) >> foundPoi
    1 * pointOfInterestRepository.save(foundPoi, 1) >> { parameters -> parameters[0] }
    actualPoi.id == ANY_POI_ID
    actualPoi.location == AMSTERDAM
  }


  def "When POI is found, it saves the original ID from tourpedia"() {
    pointOfInterestRepository.save(_,_) >> { parameters -> parameters[0] }

    when:
    PointOfInterest poi = pointOfInterestService.insertOrUpdatePoi(TOURPEDIA_ENTRY_WITHOUT_ADDRESS, AMSTERDAM)

    then:
    poi.referenceId == "tourpedia#33985"
  }

  private PointOfInterest createFakePointOfInterest () {
    new PointOfInterest(id: ANY_POI_ID)
  }

}
