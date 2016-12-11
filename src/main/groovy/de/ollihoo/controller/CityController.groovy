package de.ollihoo.controller

import de.ollihoo.domain.City
import de.ollihoo.repository.CityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CityController {

  @Autowired
  private CityRepository cityRepository

  @RequestMapping(value="cities")
  Iterable<City> getCities() {
    cityRepository.findAll()
  }

}
