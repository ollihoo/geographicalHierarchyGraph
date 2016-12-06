package de.ollihoo.tourpedia

import de.ollihoo.domain.Address
import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.AddressRepository
import de.ollihoo.repository.CityRepository
import de.ollihoo.repository.PointOfInterestRepository
import de.ollihoo.services.AddressService

class AttractionDataServiceTest extends AttractionDataServiceTestBase {
  private static TOURPEDIA_JSON_RESPONSE =
      [TOURPEDIA_ENTRY_WITHOUT_ADDRESS, TOURPEDIA_ENTRY_WITH_ADDRESS,
       TOURPEDIA_ENTRY_WITH_CITY_COUNTRY_IN_ADDRESS, TOURPEDIA_ENTRY_WITH_NETHERLANDS_AS_ADDRESS]
  public static final Long ANY_POI_ID = 12345L

  private TourpediaService tourpediaService
  private CityRepository cityRepository
  private AddressService addressService
  private PointOfInterestRepository pointOfInterestRepository
  private AttractionDataService service

  def setup() {
    tourpediaService = Mock(TourpediaService)
    cityRepository = Mock(CityRepository)
    addressService = Mock(AddressService)
    pointOfInterestRepository = Mock(PointOfInterestRepository)

    service = new AttractionDataService(
        tourpediaService: tourpediaService,
        cityRepository: cityRepository,
        addressService: addressService,
        pointOfInterestRepository: pointOfInterestRepository
    )
    cityRepository.save(_, _) >> AMSTERDAM
  }

  def "Get point of interests from tourpediaService"() {
    when:
    service.attractionsForAmsterdam

    then:
    1 * tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> TOURPEDIA_JSON_RESPONSE
  }

  def "Method parses all given elements"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> TOURPEDIA_JSON_RESPONSE
    when:
    def response = service.attractionsForAmsterdam

    then:
    response.size() == TOURPEDIA_JSON_RESPONSE.size()
  }

  def "When POI is new, it is saved" () {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]

    when:
    service.attractionsForAmsterdam

    then:
    1 * pointOfInterestRepository.findByName(TOURPEDIA_ENTRY_WITHOUT_ADDRESS.name) >> null
    1 * pointOfInterestRepository.save(_, 1)
  }

  def "When POI already exists, it is updated" () {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]
    PointOfInterest foundPoi = createFakePointOfInterest()

    when:
    service.attractionsForAmsterdam

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
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]
    PointOfInterest foundPoi = createFakePointOfInterest()

    when:
    service.attractionsForAmsterdam

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
    Address foundAddress = new Address()
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_ADDRESS]
    addressService.getOrCreateAddress(_,_) >> foundAddress

    when:
    service.attractionsForAmsterdam

    then:
    1 * pointOfInterestRepository.findByName(TOURPEDIA_ENTRY_WITH_ADDRESS.name) >> foundPoi
    1 * pointOfInterestRepository.save(foundPoi, 1)
    foundPoi.id == ANY_POI_ID
    foundPoi.location == foundAddress
  }


  def "When POI is found, it saves the original ID from tourpedia"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]
    PointOfInterest actualPOI = null

    when:
    service.attractionsForAmsterdam

    then:
    pointOfInterestRepository.save(_, 1) >> { parameters -> actualPOI = parameters[0] }
    actualPOI.referenceId == "tourpedia#33985"
  }

  private PointOfInterest createFakePointOfInterest () {
    new PointOfInterest(id: ANY_POI_ID)
  }

}
