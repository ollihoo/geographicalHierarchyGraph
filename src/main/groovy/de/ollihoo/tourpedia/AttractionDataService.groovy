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

        def amsterdam = getOrCreateCity("Amsterdam")

        json.collect { entry ->
            def address = null
            if (entry.address) {
                String trimmedAddress = removeUnexpectedEntriesFromStreet(entry.address)
                address = trimmedAddress ?
                    address = new Address(street: trimmedAddress, zip: "", location: amsterdam): amsterdam
            } else {
                address = amsterdam
            }

            new PointOfInterest(name: entry.name, type: entry.category, lat: entry.lat, lng: entry.lng,
                    location: address, referenceId: "tourpedia#" + entry.id
            )
        }
    }

    private City getOrCreateCity(String cityName) {
        def city = cityRepository.findByName(cityName)
        if (! city) {
            city = cityRepository.save(new City(name: cityName), 1)
        }
        city
    }

    private removeUnexpectedEntriesFromStreet(String input) {
        input.replaceAll(", Amsterdam", "").replaceAll(/,? *Netherlands/, "")
    }

}
