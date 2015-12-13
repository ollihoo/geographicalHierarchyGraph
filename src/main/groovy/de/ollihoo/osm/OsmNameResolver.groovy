package de.ollihoo.osm

import de.ollihoo.domain.City
import org.springframework.stereotype.Service

@Service
class OsmNameResolver {

    String parsePoiName(String name) {
        splitCombinedName(name)[0]
    }

    City parseCity(String name) {
        def addr = splitCombinedName(name)
        switch (addr.length) {
            case 9:
                return new City(
                        housenumber: addr[1],
                        street: addr[2],
                        district: addr[4],
                        sector: addr[5],
                        name: addr[6],
                        zip: addr[7]
                )
            case 8:
                return new City(
                        housenumber: addr[1],
                        street: addr[2],
                        district: addr[3],
                        sector: addr[4],
                        name: addr[5],
                        zip: addr[6]
                )
            case 7:
                return new City(
                        street: addr[1],
                        district: addr[2],
                        sector: addr[3],
                        name: addr[4],
                        zip: addr[5]
                )
            case 6:
                return new City(
                        street: addr[1],
                        sector: addr[2],
                        name: addr[3],
                        zip: addr[4]
                )
            case 5:
                return new City(
                        sector: addr[1],
                        name: addr[2],
                        zip: addr[3]
                )
            case 4:
                return new City(
                        sector: addr[1],
                        name: addr[2],
                )
            case 3:
                return new City(
                        name: addr[1],
                )
        }
        return null
    }

    private splitCombinedName(String input) {
        input.split("\t")[0].replaceAll(" ?, ?", ",").split(",")
    }

}
