package de.ollihoo.tourpedia

import de.ollihoo.repository.CityRepository
import de.ollihoo.services.AddressService

class AttractionDataServiceCityTest extends AttractionDataServiceTestBase {
  private TourpediaService tourpediaService
  private CityRepository cityRepository
  private AddressService addressService
  private AttractionDataService service

  def setup() {
    tourpediaService = Mock(TourpediaService)
    cityRepository = Mock(CityRepository)
    addressService = Mock(AddressService)
    service = new AttractionDataService(
        tourpediaService: tourpediaService,
        cityRepository: cityRepository,
        addressService: addressService
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

}
