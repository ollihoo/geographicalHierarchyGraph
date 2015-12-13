package de.ollihoo.osm

import de.ollihoo.domain.PointOfInterest
import de.ollihoo.repository.PointOfInterestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ListingService {

    @Autowired
    OsmNameResolver osmNameResolver

    @Autowired
    PointOfInterestRepository pointOfInterestRepository

    List<PointOfInterest> parsePointOfInterests() {
        URL url = this.class.getClassLoader().getResource("PointOfInterests.csv")
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
