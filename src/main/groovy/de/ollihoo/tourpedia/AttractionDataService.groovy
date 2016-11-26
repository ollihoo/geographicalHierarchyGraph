package de.ollihoo.tourpedia

import de.ollihoo.domain.Address
import de.ollihoo.domain.City
import de.ollihoo.domain.PointOfInterest
import org.springframework.beans.factory.annotation.Autowired

class AttractionDataService {

    @Autowired
    private TourpediaService tourpediaService

    List<PointOfInterest> getAttractions() {

        def json = tourpediaService.getJsonResponseFor("Amsterdam", "attraction")

        def amsterdam = new City(name: "Amsterdam")

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

    private removeUnexpectedEntriesFromStreet(String input) {
        input.replaceAll(", Amsterdam", "").replaceAll(/,? *Netherlands/, "")
    }

}
