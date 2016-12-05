package de.ollihoo.tourpedia

import de.ollihoo.domain.Address
import de.ollihoo.repository.AddressRepository
import de.ollihoo.repository.CityRepository

class AttractionDataServiceAddressTest extends AttractionDataServiceTestBase {
  private static
  final Address VAN_KINSBERGENSTRAAT_6 = new Address(street: "Van Kinsbergenstraat 6", location: AMSTERDAM)

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

  def "When Amsterdam is known, return it without saving"() {
    when:
    service.attractionsForAmsterdam

    then:
    1 * cityRepository.findByName("Amsterdam") >> AMSTERDAM
    0 * cityRepository.save(_, _)
  }

  def "When no street is given, response has city as location"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]
    when:
    def response = service.attractionsForAmsterdam

    then:
    isCityOfAmsterdam response[0].location
  }

  def "When street is given, response has address as location"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_ADDRESS]
    addressRepository.save(_, 1) >> { parameters -> parameters[0] }

    when:
    def response = service.attractionsForAmsterdam

    then:
    isAmsterdamWithStreet(response[0].location, "Van Kinsbergenstraat 6")
  }

  def "When street is given, a response address is tested to be in database"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_ADDRESS]
    when:
    service.attractionsForAmsterdam

    then:
    1 * addressRepository.findByStreetAndLocation("Van Kinsbergenstraat 6", AMSTERDAM)
  }

  def "When street is found in database, entry is returned without saving"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_ADDRESS]
    addressRepository.findByStreetAndLocation("Van Kinsbergenstraat 6", AMSTERDAM) >> VAN_KINSBERGENSTRAAT_6

    when:
    def response = service.attractionsForAmsterdam

    then:
    response[0].location == VAN_KINSBERGENSTRAAT_6
  }

  def "When street is not found in database, entry is saved and returned"() {
    addressRepository.findByStreetAndLocation("Van Kinsbergenstraat 6", AMSTERDAM) >> null
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_ADDRESS]
    def savedAddress = null

    when:
    service.attractionsForAmsterdam

    then:
    1 * addressRepository.save(_, 1) >> { parameters ->
      savedAddress = (parameters[0].street == "Van Kinsbergenstraat 6") ? VAN_KINSBERGENSTRAAT_6 : null
      savedAddress
    }
    savedAddress == VAN_KINSBERGENSTRAAT_6
  }

  def "When 'Amsterdam, Netherlands' is in street name, remove it"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_CITY_COUNTRY_IN_ADDRESS]
    addressRepository.save(_, 1) >> { parameters -> parameters[0] }

    when:
    def responses = service.attractionsForAmsterdam

    then:
    responses[0].location.street == "Eva Besnyöstraat 289"
  }

  def "When 'Netherlands' is street name, use city of Amsterdam"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_NETHERLANDS_AS_ADDRESS]
    addressRepository.save(_, 1) >> { parameters -> parameters[0] }

    when:
    def responses = service.attractionsForAmsterdam

    then:
    isCityOfAmsterdam(responses[0].location)
  }

}
