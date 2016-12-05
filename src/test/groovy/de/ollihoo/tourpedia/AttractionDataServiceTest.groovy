package de.ollihoo.tourpedia

import de.ollihoo.repository.AddressRepository
import de.ollihoo.repository.CityRepository

class AttractionDataServiceTest extends AttractionDataServiceTestBase {
  private static TOURPEDIA_JSON_RESPONSE =
      [TOURPEDIA_ENTRY_WITHOUT_ADDRESS, TOURPEDIA_ENTRY_WITH_ADDRESS,
       TOURPEDIA_ENTRY_WITH_CITY_COUNTRY_IN_ADDRESS, TOURPEDIA_ENTRY_WITH_NETHERLANDS_AS_ADDRESS]


  private TourpediaService tourpediaService
  private CityRepository cityRepository
  private AddressRepository addressRepository
  private AttractionDataService service

  def setup() {
    tourpediaService = Mock(TourpediaService)
    cityRepository = Mock(CityRepository)
    addressRepository = Mock(AddressRepository)
    service = new AttractionDataService(
        tourpediaService: tourpediaService,
        cityRepository: cityRepository,
        addressRepository: addressRepository
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

  def "When POI is found, it saves the original ID from tourpedia"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]

    when:
    def response = service.attractionsForAmsterdam

    then:
    response[0].referenceId == "tourpedia#33985"
  }

}
