package de.ollihoo.tourpedia

import de.ollihoo.domain.Address
import de.ollihoo.domain.City
import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.CityRepository
import org.springframework.beans.factory.annotation.Autowired

class AttractionDataService {

    @Autowired
    private TourpediaService tourpediaService

    @Autowired
    private CityRepository cityRepository

    List<PointOfInterest> getAttractionsForAmsterdam() {

        def json = tourpediaService.getJsonResponseFor("Amsterdam", "attraction")

        City amsterdam = getOrCreateCity("Amsterdam")

        json.collect { entry ->
            Address address = getOrCreateAddress(amsterdam, entry)

            new PointOfInterest(name: entry.name, type: entry.category, lat: entry.lat, lng: entry.lng,
                    location: address?:amsterdam, referenceId: "tourpedia#" + entry.id
            )
        }
    }

    private City getOrCreateCity(String cityName) {
        cityRepository.findByName(cityName)?: cityRepository.save(new City(name: cityName), 1)
    }

    private Address getOrCreateAddress(City city, entry) {
        String trimmedAddress = removeUnexpectedEntriesFromStreet(entry.address)
        trimmedAddress ? new Address(street: trimmedAddress, zip: "", location: city) : null
    }

    private removeUnexpectedEntriesFromStreet(String input) {
        if (input) {
            return input.replaceAll(", Amsterdam", "").replaceAll(/,? *Netherlands/, "")
        }
        null
    }

}
