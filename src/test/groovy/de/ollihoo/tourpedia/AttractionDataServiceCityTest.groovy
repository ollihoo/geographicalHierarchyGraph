package de.ollihoo.tourpedia

import de.ollihoo.repository.AddressRepository
import de.ollihoo.repository.CityRepository

class AttractionDataServiceCityTest extends AttractionDataServiceTestBase {
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

  def "When Amsterdam is unknown, save it to the database"() {
    def usedCity = null
    when:
    service.attractionsForAmsterdam

    then:
    1 * cityRepository.findByName("Amsterdam") >> null
    1 * cityRepository.save(_, 1) >> { parameters ->
      usedCity = parameters[0].name == "Amsterdam" ? AMSTERDAM : null
    }
    usedCity == AMSTERDAM
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

}
