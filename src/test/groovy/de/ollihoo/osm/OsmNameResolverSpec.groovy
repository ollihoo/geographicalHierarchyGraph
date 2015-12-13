package de.ollihoo.osm

import de.ollihoo.domain.City
import spock.lang.Specification
import spock.lang.Unroll

class OsmNameResolverSpec extends Specification {

    OsmNameResolver resolver = new OsmNameResolver()

    @Unroll
    def "all valid combinations are parsed to city"() {
        when:
        City city = resolver.parseCity(input)

        then:
        city.name == expectedCity
        city.sector == expectedSector
        city.zip == expectedZip
        city.street == expectedStreet
        city.district == expectedDistrict
        city.housenumber == expectedHNo

        where:
        input                                                                                                            | expectedCity | expectedSector               | expectedZip | expectedStreet       | expectedDistrict    | expectedHNo
        "Wannsee, Steglitz-Zehlendorf, Berlin, Deutschland"                                                              | "Berlin"     | "Steglitz-Zehlendorf"        | null        | null                 | null                | null
        "Scheunenviertel, Mitte, Berlin, 10119, Deutschland"                                                             | "Berlin"     | "Mitte"                      | "10119"     | null                 | null                | null
        "Kulturhaus der Russischen Botschaft, Wilhelmstraße, Mitte, Berlin, 10117, Deutschland"                          | "Berlin"     | "Mitte"                      | "10117"     | "Wilhelmstraße"      | null                | null
        "Schloss Charlottenburg, Spandauer Damm, Charlottenburg, Charlottenburg-Wilmersdorf, Berlin, 14059, Deutschland" | "Berlin"     | "Charlottenburg-Wilmersdorf" | "14059"     | "Spandauer Damm"     | "Charlottenburg"    | null
        "Schloss Schönhausen, 1, Tschaikowskistraße, Niederschönhausen, Pankow, Berlin, 13156, Deutschland"              | "Berlin"     | "Pankow"                     | "13156"     | "Tschaikowskistraße" | "Niederschönhausen" | "1"
        "Fort Hahneberg, 50, Hahnebergweg, Wochendsiedlung, Staaken, Spandau, Berlin, 13591, Deutschland"                | "Berlin"     | "Spandau"                    | "13591"     | "Hahnebergweg"       | "Staaken"           | "50"
    }


}
