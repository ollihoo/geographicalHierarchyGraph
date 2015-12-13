package de.ollihoo.osm

import groovy.json.JsonSlurper
import spock.lang.Specification


class OpenStreetMapSpec extends Specification {

    def "Request for a special entity is answered in the described way" () {
        given:
        def url = new URL("http://nominatim.openstreetmap.org/search.php?format=json&q=Berlin,%20Australische%20Botschaft")

        when:
        def result = new JsonSlurper().parse(url, "utf-8")

        then:
        result.type[0] == "embassy"
        result.display_name[0] == "Australische Botschaft, Wallstraße, Scheunenviertel, Mitte, Berlin, 10179, Deutschland"
        result.lat[0] == "52.5122655"
        result.lon[0] == "13.4093648"
    }

    def "Request with a senseless entity is answered in the described way" () {
        given:
        def url = new URL("http://nominatim.openstreetmap.org/search.php?format=json&q=qweqweqwe")

        when:
        def result = new JsonSlurper().parse(url)

        then:
        result == []
    }

}
