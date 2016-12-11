package de.ollihoo.tourpedia

import de.ollihoo.domain.Address
import de.ollihoo.domain.AdministrativeUnit
import de.ollihoo.domain.City
import de.ollihoo.domain.PointOfInterest
import de.ollihoo.osm.OsmService
import de.ollihoo.services.AddressService
import de.ollihoo.services.CityService
import de.ollihoo.services.PointOfInterestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AttractionDataService {
  public static final String TOURPEDIA_PREFIX = "tourpedia#"

  @Autowired
  private TourpediaService tourpediaService
  @Autowired
  private CityService cityService
  @Autowired
  private AddressService addressService
  @Autowired
  private PointOfInterestService pointOfInterestService
  @Autowired
  private OsmService osmService

  List<PointOfInterest> getAttractionsForAmsterdam() {
  String cityName = "Amsterdam"

    def json = tourpediaService.getJsonResponseFor(cityName, "attraction")

    City amsterdam = cityService.getCity(cityName)
    if (! amsterdam) {
      PointOfInterest poi = osmService.getPointOfInterestByRequest(cityName)
      City city = new City(name: poi.name, lat: poi.lat, lng: poi.lng)
      amsterdam = cityService.createOrUpdateCity(city)
    }

    json.collect { entry ->
      Address address = addressService.getOrCreateAddress(amsterdam, entry)
      AdministrativeUnit location = address ?: amsterdam
      PointOfInterest poi = new PointOfInterest(
          name: entry.name,
          type: entry.category,
          lat: entry.lat, lng: entry.lng,
          location: location,
          referenceId: TOURPEDIA_PREFIX + entry.id
      )
      pointOfInterestService.insertOrUpdatePoi(poi)
    }
  }



}
