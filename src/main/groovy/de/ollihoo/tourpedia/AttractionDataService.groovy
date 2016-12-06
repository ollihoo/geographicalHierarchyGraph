package de.ollihoo.tourpedia

import de.ollihoo.domain.Address
import de.ollihoo.domain.City
import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.CityRepository
import de.ollihoo.repository.PointOfInterestRepository
import de.ollihoo.services.AddressService
import org.springframework.beans.factory.annotation.Autowired

class AttractionDataService {

    public static final String TOURPEDIA_PREFIX = "tourpedia#"

    @Autowired
    private TourpediaService tourpediaService

    @Autowired
    private CityRepository cityRepository

    @Autowired
    private AddressService addressService

    @Autowired
    PointOfInterestRepository pointOfInterestRepository


    List<PointOfInterest> getAttractionsForAmsterdam() {

        def json = tourpediaService.getJsonResponseFor("Amsterdam", "attraction")

        City amsterdam = getOrCreateCity("Amsterdam")

        json.collect { entry ->
            Address address = addressService.getOrCreateAddress(amsterdam, entry)

            PointOfInterest poi = pointOfInterestRepository.findByName(entry.name)
            if (poi) {
                poi.name = entry.name
                poi.type = entry.category
                poi.lat = entry.lat
                poi.lng = entry.lng
                poi.location = address?:amsterdam
                poi.referenceId = TOURPEDIA_PREFIX+entry.id+""
                pointOfInterestRepository.save(poi, 1)
            } else {
                pointOfInterestRepository.save(new PointOfInterest(name: entry.name, type: entry.category, lat: entry.lat, lng: entry.lng,
                    location: address ?: amsterdam, referenceId: TOURPEDIA_PREFIX + entry.id
                ), 1)
            }

        }
    }

    private City getOrCreateCity(String cityName) {
        cityRepository.findByName(cityName) ?: cityRepository.save(new City(name: cityName), 1)
    }


}
