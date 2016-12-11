package de.ollihoo.services

import de.ollihoo.domain.City
import de.ollihoo.repository.CityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CityService {

  @Autowired
  private CityRepository cityRepository

  City getOrCreateCity(String cityName) {
    cityRepository.findByName(cityName) ?: cityRepository.save(new City(name: cityName), 1)
  }
}
