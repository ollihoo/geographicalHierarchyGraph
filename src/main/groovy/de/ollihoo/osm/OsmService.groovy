package de.ollihoo.osm

import de.ollihoo.domain.PointOfInterest
import groovy.json.JsonSlurper
import org.springframework.stereotype.Service

@Service
class OsmService {

    private BASE_URL = "http://nominatim.openstreetmap.org/search.php?format=json"

    PointOfInterest getPointOfInterestByRequest(String name) {
        def requestEntry = name? URLEncoder.encode(name, "utf-8"):""
        def url = new URL("$BASE_URL&q=$requestEntry")
        def result = new JsonSlurper().parse(url)
        if (result != []) {
            new PointOfInterest(
                    name: result.display_name[0],
                    lat: new BigDecimal(result.lat[0]),
                    lng: new BigDecimal(result.lon[0]),
                    type: result.type[0]
            )
        } else {
            null
        }
    }
}
