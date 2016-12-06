package de.ollihoo.tourpedia

import de.ollihoo.domain.Address
import de.ollihoo.repository.CityRepository
import de.ollihoo.repository.PointOfInterestRepository
import de.ollihoo.services.AddressService

class AttractionDataServiceAddressTest extends AttractionDataServiceTestBase {
  private static
  final Address VAN_KINSBERGENSTRAAT_6 = new Address(street: "Van Kinsbergenstraat 6", location: AMSTERDAM)

  private TourpediaService tourpediaService
  private CityRepository cityRepository
  private AddressService addressService
  private AttractionDataService service
  private PointOfInterestRepository pointOfInterestRepository

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

  def "When Amsterdam is known, return it without saving"() {
    when:
    service.attractionsForAmsterdam

    then:
    1 * cityRepository.findByName("Amsterdam") >> AMSTERDAM
    0 * cityRepository.save(_, _)
  }

  def "When address is null, add Amsterdam as location" () {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]
    pointOfInterestRepository.save(_,_) >> { parameters -> parameters[0] }

    when:
    def responses = service.attractionsForAmsterdam

    then:
    responses[0].location == AMSTERDAM
  }

  def "When address is set, it is used as location"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_NETHERLANDS_AS_ADDRESS]
    addressService.getOrCreateAddress(_, _) >> VAN_KINSBERGENSTRAAT_6
    pointOfInterestRepository.save(_,_) >> { parameters -> parameters[0] }

    when:
    def responses = service.attractionsForAmsterdam

    then:
    responses[0].location == VAN_KINSBERGENSTRAAT_6
  }

}
