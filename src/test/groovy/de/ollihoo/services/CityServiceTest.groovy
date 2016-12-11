package de.ollihoo.services

import de.ollihoo.domain.City
import de.ollihoo.repository.CityRepository
import de.ollihoo.tourpedia.AttractionDataServiceTestBase

class CityServiceTest extends AttractionDataServiceTestBase {
  public static final City ANY_NEW_CITY = new City(name: "Amsterdam")
  private CityRepository cityRepository
  private CityService service

  def setup() {
    cityRepository = Mock(CityRepository)
    service = new CityService(cityRepository: cityRepository)
    cityRepository.save(_, _) >> AMSTERDAM
  }

  def "When Amsterdam is unknown, it returns null" () {
    def usedCity = null
    when:
    def result = service.getCity("Amsterdam")

    then:
    1 * cityRepository.findByName("Amsterdam") >> null
    result == null
  }


  def "When Amsterdam is searched, it uses cityRepository" () {
    def usedCity = null
    when:
    def result = service.getCity("Amsterdam")

    then:
    1 * cityRepository.findByName("Amsterdam")
  }

  def "When new city is given, it will be saved" () {
    when:
    service.createOrUpdateCity(ANY_NEW_CITY)

    then:
    1 * cityRepository.save(ANY_NEW_CITY, 1)
  }

}
