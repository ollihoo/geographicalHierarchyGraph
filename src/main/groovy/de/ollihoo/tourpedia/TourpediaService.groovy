package de.ollihoo.tourpedia

import groovy.json.JsonSlurper
import org.springframework.stereotype.Service

@Service
class TourpediaService {


    def getJsonResponseFor(String city, String type) {
        URL requestUrl = new URL("http://tour-pedia.org/api/getPlaces?location=$city&category=$type")
        new JsonSlurper().parse(requestUrl)
    }
}
