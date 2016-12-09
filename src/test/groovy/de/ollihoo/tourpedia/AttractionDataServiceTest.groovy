package de.ollihoo.tourpedia

import de.ollihoo.domain.Address
import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.CityRepository
import de.ollihoo.services.AddressService
import de.ollihoo.services.PointOfInterestService

class AttractionDataServiceTest extends AttractionDataServiceTestBase {
  private static TOURPEDIA_JSON_RESPONSE =
      [TOURPEDIA_ENTRY_WITHOUT_ADDRESS, TOURPEDIA_ENTRY_WITH_ADDRESS,
       TOURPEDIA_ENTRY_WITH_CITY_COUNTRY_IN_ADDRESS, TOURPEDIA_ENTRY_WITH_NETHERLANDS_AS_ADDRESS]
  private static
  final Address VAN_KINSBERGENSTRAAT_6 = new Address(street: "Van Kinsbergenstraat 6", location: AttractionDataServiceTestBase.AMSTERDAM)

  private TourpediaService tourpediaService
  private CityRepository cityRepository
  private AddressService addressService
  private PointOfInterestService pointOfInterestService
  private AttractionDataService service

  def setup() {
    tourpediaService = Mock(TourpediaService)
    cityRepository = Mock(CityRepository)
    addressService = Mock(AddressService)
    pointOfInterestService = Mock(PointOfInterestService)

    service = new AttractionDataService(
        tourpediaService: tourpediaService,
        cityRepository: cityRepository,
        addressService: addressService,
        pointOfInterestService: pointOfInterestService
    )
    cityRepository.save(_, _) >> AMSTERDAM
  }

  def "Get point of interests from tourpediaService"() {
    when:
    service.attractionsForAmsterdam

    then:
    1 * tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> TOURPEDIA_JSON_RESPONSE
  }

  def "When Amsterdam is known, return it without saving"() {
    when:
    service.attractionsForAmsterdam

    then:
    1 * cityRepository.findByName("Amsterdam") >> AMSTERDAM
    0 * cityRepository.save(_, _)
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
    1 * pointOfInterestService.insertOrUpdatePoi(_)
  }

  def "When address is null, add Amsterdam as location"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]
    pointOfInterestService.insertOrUpdatePoi(_) >> { parameters -> parameters[0] }

    when:
    def actualPointOfInterests = service.attractionsForAmsterdam

    then:
    actualPointOfInterests[0].location == AMSTERDAM
  }

  def "When address is set, it is used as location"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_NETHERLANDS_AS_ADDRESS]
    addressService.getOrCreateAddress(_, _) >> VAN_KINSBERGENSTRAAT_6
    pointOfInterestService.insertOrUpdatePoi(_) >> {parameters -> parameters[0]}

    when:
    def actualPointOfInterests = service.attractionsForAmsterdam

    then:
    actualPointOfInterests[0].location == VAN_KINSBERGENSTRAAT_6
  }

  def "When POI is found, it saves the original ID from tourpedia"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]
    pointOfInterestService.insertOrUpdatePoi(_)  >> { parameters -> parameters[0] }

    when:
    def actualPointOfInterests = service.attractionsForAmsterdam

    then:

    actualPointOfInterests[0].referenceId == "tourpedia#" + TOURPEDIA_ENTRY_WITHOUT_ADDRESS.id
  }


}
