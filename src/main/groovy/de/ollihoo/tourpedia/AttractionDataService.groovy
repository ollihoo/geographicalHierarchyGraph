package de.ollihoo.tourpedia

import de.ollihoo.domain.Address
import de.ollihoo.domain.AdministrativeUnit
import de.ollihoo.domain.City
import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.CityRepository
import de.ollihoo.repository.PointOfInterestRepository
import de.ollihoo.services.AddressService
import de.ollihoo.services.PointOfInterestService
import org.springframework.beans.factory.annotation.Autowired

class AttractionDataService {

  @Autowired
  private TourpediaService tourpediaService

  @Autowired
  private CityRepository cityRepository

  @Autowired
  private AddressService addressService

  @Autowired
  private PointOfInterestService pointOfInterestService

  List<PointOfInterest> getAttractionsForAmsterdam() {

    def json = tourpediaService.getJsonResponseFor("Amsterdam", "attraction")

    City amsterdam = getOrCreateCity("Amsterdam")

    json.collect { entry ->
      Address address = addressService.getOrCreateAddress(amsterdam, entry)
      AdministrativeUnit location = address ?: amsterdam
      pointOfInterestService.insertOrUpdatePoi(entry, location)
    }
  }

  private City getOrCreateCity(String cityName) {
    cityRepository.findByName(cityName) ?: cityRepository.save(new City(name: cityName), 1)
  }


}
