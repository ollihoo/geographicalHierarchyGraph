package de.ollihoo.services

import de.ollihoo.domain.City
import de.ollihoo.repository.CityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CityService {

  @Autowired
  private CityRepository cityRepository

  City getCity(String cityName) {
    cityRepository.findByName(cityName)
  }

  City createOrUpdateCity(City city) {
    cityRepository.save(city, 1)
  }
}
