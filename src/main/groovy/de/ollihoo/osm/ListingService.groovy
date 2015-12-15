package de.ollihoo.osm

import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.PointOfInterestRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ListingService {
    private static final Logger LOG = LoggerFactory.getLogger(ListingService)

    @Autowired
    OsmNameResolver osmNameResolver

    @Autowired
    OsmService osmService

    @Autowired
    PointOfInterestRepository pointOfInterestRepository

    List<PointOfInterest> parseRawPointOfInterests(String city) {
        if (city =~ /. , \//) {
            return []
        }
        def pattern = city.toLowerCase()
        URL url = this.class.getClassLoader().getResource("cities/$pattern/poi.csv")
        def resultList = []
        url.eachLine { line ->
            def poi = osmService.getPointOfInterestByRequest("Hamburg, " + line)
            if (poi) {
                resultList << poi
            } else {
                LOG.info("NO ENTRY: $line")
            }
        }
        resultList
    }

    List<PointOfInterest> parsePointOfInterestsWithCoordinates(String city) {
        if (city =~ /. , \//) {
            return []
        }
        def pattern = city.toLowerCase()
        URL url = this.class.getClassLoader().getResource("cities/$pattern/PointOfInterests.csv")
        def rows = []
        url.text.eachLine { line -> rows << line }
        rows.collect { line ->
            findOrCreatePOI(line.split("\t"))
        }
    }

    private PointOfInterest findOrCreatePOI(def poiData) {
        String name = osmNameResolver.parsePoiName(poiData[0])
        def poi = pointOfInterestRepository.findByName(name)?:
                new PointOfInterest(
                name: name,
                lat: new BigDecimal(poiData[1]),
                lng: new BigDecimal(poiData[2]),
                type: poiData[3],
                location: osmNameResolver.parseCombinedAddress(poiData[0])
        )
        pointOfInterestRepository.save(poi, 1)
    }

}
