package de.ollihoo.osm

import de.ollihoo.domain.Address
import de.ollihoo.domain.City
import de.ollihoo.domain.District
import de.ollihoo.domain.Sector
import de.ollihoo.repository.AddressRepository
import de.ollihoo.repository.CityRepository
import de.ollihoo.repository.DistrictRepository
import de.ollihoo.repository.SectorRepository
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

class OsmNameResolverSpec extends Specification {

    OsmNameResolver resolver = new OsmNameResolver()

    def setup() {
        resolver.cityRepository = Mock(CityRepository)
        resolver.addressRepository = Mock(AddressRepository)
        resolver.districtRepository = Mock(DistrictRepository)
        resolver.sectorRepository = Mock(SectorRepository)

        resolver.cityRepository.findByName(_) >> null
        resolver.addressRepository.findByZipAndStreet(_, _) >> null
        resolver.districtRepository.findByName(_) >> null
        resolver.sectorRepository.findByName(_) >> null

        resolver.addressRepository.save(_, 1) >> new Address()
        resolver.cityRepository.save(_, 1) >> new City()
        resolver.districtRepository.save(_, 1) >> new District()
        resolver.sectorRepository.save(_, 1) >> new Sector()
    }

    def "parsing poi combined name with 9 entries returns address"() {
        given:
        def name = "Fort Hahneberg, 50, Hahnebergweg, Wochendsiedlung, Staaken, Spandau, Berlin, 13591, Deutschland"

        when:
        def result = resolver.parseCombinedAddress(name)

        then:
        result instanceof Address
    }

    def "parsing poi combined name with 8 entries returns address"() {
        given:
        def name = "Schloss Schönhausen, 1, Tschaikowskistraße, Niederschönhausen, Pankow, Berlin, 13156, Deutschland"

        when:
        def result = resolver.parseCombinedAddress(name)

        then:
        result instanceof Address
    }

    def "parsing poi combined name with 7 entries returns address"() {
        given:
        def name = "Schloss Charlottenburg, Spandauer Damm, Charlottenburg, Charlottenburg-Wilmersdorf, Berlin, 14059, Deutschland"

        when:
        def result = resolver.parseCombinedAddress(name)

        then:
        result instanceof Address
    }

    def "parsing poi combined name with 6 entries returns address"() {
        given:
        def name = "Kulturhaus der Russischen Botschaft, Wilhelmstraße, Mitte, Berlin, 10117, Deutschland"

        when:
        def result = resolver.parseCombinedAddress(name)

        then:
        result instanceof Address
    }

    def "parsing poi combined name with 5 entries returns sector"() {
        given:
        def name = "Scheunenviertel, Mitte, Berlin, 10119, Deutschland"

        when:
        def result = resolver.parseCombinedAddress(name)

        then:
        result instanceof Sector
    }

    def "parsing poi combined name with 4 entries returns Sector"() {
        given:
        def name = "Wannsee, Steglitz-Zehlendorf, Berlin, Deutschland"

        when:
        def result = resolver.parseCombinedAddress(name)

        then:
        result instanceof Sector
    }

}
