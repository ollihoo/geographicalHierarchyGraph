package de.ollihoo.osm

import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.PointOfInterestRepository
import groovy.json.JsonSlurper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OsmService {
    private static final Logger LOG = LoggerFactory.getLogger(OsmService)
    private BASE_URL = "http://nominatim.openstreetmap.org/search.php?format=json"

    @Autowired
    OsmNameResolver osmNameResolver
    @Autowired
    PointOfInterestRepository pointOfInterestRepository

    PointOfInterest getPointOfInterestByRequest(String name) {
        def requestEntry = name? URLEncoder.encode(name, "utf-8"):""
        def url = new URL("$BASE_URL&q=$requestEntry")
        def result = new JsonSlurper().parse(url)
        if (result != []) {
            String combinedName = result.display_name[0]
            def poi = new PointOfInterest(
                    name: osmNameResolver.parsePoiName(combinedName),
                    lat: new BigDecimal(result.lat[0]),
                    lng: new BigDecimal(result.lon[0]),
                    type: result.type[0],
                    location: osmNameResolver.parseCombinedAddress(combinedName)
            )
            LOG.info("$combinedName\t${result.lat[0]}\t${result.lon[0]}\t${result.type[0]}")
            pointOfInterestRepository.save(poi, 1)
        } else {
            null
        }
    }


}
