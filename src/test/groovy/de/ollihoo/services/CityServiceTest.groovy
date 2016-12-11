package de.ollihoo.services

import de.ollihoo.repository.CityRepository
import de.ollihoo.tourpedia.AttractionDataServiceTestBase

class CityServiceTest extends AttractionDataServiceTestBase {
  private CityRepository cityRepository
  private CityService service

  def setup() {
    cityRepository = Mock(CityRepository)
    service = new CityService(cityRepository: cityRepository)
    cityRepository.save(_, _) >> AMSTERDAM
  }

  def "When Amsterdam is unknown, save it to the database"() {
    def usedCity = null
    when:
    service.getOrCreateCity("Amsterdam")

    then:
    1 * cityRepository.findByName("Amsterdam") >> null
    1 * cityRepository.save(_, 1) >> { parameters ->
      usedCity = parameters[0].name == "Amsterdam" ? AMSTERDAM : null
    }
    usedCity == AMSTERDAM
  }

  def "When Amsterdam is known, return it without saving"() {
    when:
    service.getOrCreateCity("Amsterdam")

    then:
    1 * cityRepository.findByName("Amsterdam") >> AMSTERDAM
    0 * cityRepository.save(_, _)
  }

}
