package de.ollihoo.tourpedia

import de.ollihoo.domain.Address
import de.ollihoo.domain.AdministrativeUnit
import de.ollihoo.domain.City
import spock.lang.Specification


class AttractionDataServiceTest extends Specification {

    private TourpediaService tourpediaService
    private AttractionDataService service

    def setup() {
        tourpediaService = Mock(TourpediaService)
        service = new AttractionDataService(tourpediaService: tourpediaService)

        tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> createJsonRepsonse()
    }


    def "get point of interests from tourpediaService" () {
        when:
        service.attractions

        then:
        1 * tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> createJsonRepsonse()
    }

    def "response delivers expected number of elements" () {
        when:
        def response = service.attractions

        then:
        response.size() == 4
    }

    def "response delivers correct address" () {
        when:
        def response = service.attractions

        then:
        isCityOfAmsterdam response[0].location
        isAmsterdamWithStreet(response[1].location, "Van Kinsbergenstraat 6")
    }

    def "POI contains original ID" () {
        when:
        def response = service.attractions

        then:
        response[0].referenceId == "tourpedia#33985"
    }

    def "Amsterdam, Netherlands is cut out of the street entry" () {
        when:
        def responses = service.attractions

        then:
        responses[2].location.street == "Eva Besnyöstraat 289"
        isCityOfAmsterdam(responses[3].location)
    }


    private boolean isAmsterdamWithStreet(AdministrativeUnit location, String expectedStreet) {
        location instanceof Address && location.street == expectedStreet && isCityOfAmsterdam(location.location)
    }

    private boolean isCityOfAmsterdam(AdministrativeUnit location) {
        location instanceof City && location.name == "Amsterdam"
    }


    private  createJsonRepsonse() {
        [[address: null, category:"attraction", details:"http://tour-pedia.org/api/getPlaceDetails?id=33985",
          id:33985, lat:52.377262574894, lng:4.8580104817941, location:"Amsterdam", name:"Thenextweb",
          numReviews:1, originalId:"5178d2aee4b0d969ecd65d3f", polarity:5,
          reviews:"http://tour-pedia.org/api/getReviewsByPlaceId?placeId=33985", subCategory:"Auditorium"],
         [address:"Van Kinsbergenstraat 6", category:"attraction", details:"http://tour-pedia.org/api/getPlaceDetails?id=33989",
          id:33989, lat:52.368381396984, lng:4.8630453502768, location:"Amsterdam", name:"SGI Amsterdam Cultural Centre",
          numReviews:3, originalId:"4e8c99878b81e678900e7744", polarity:7,
          reviews:"http://tour-pedia.org/api/getReviewsByPlaceId?placeId=33989", subCategory:"Spiritual Center"],
        [address:"Eva Besnyöstraat 289, Amsterdam, Netherlands", category:"attraction", details:"http://tour-pedia.org/api/getPlaceDetails?id=243966",
         id:243966, lat:52.351137, lng:5.005955, location:"Amsterdam", name:"freelance dee jay",
         numReviews:3, originalId:"4e8c99878b81900e7744", polarity:3,
         reviews:"http://tour-pedia.org/api/getReviewsByPlaceId?placeId=243966", subCategory:"Spiritual Center"],
         [address:"Netherlands", category:"attraction", details:"http://tour-pedia.org/api/getPlaceDetails?id=243966",
          id:243966, lat:52.351137, lng:5.005955, location:"Amsterdam", name:"freelance dee jay",
          numReviews:3, originalId:"4e8c99878b81900e7744", polarity:3,
          reviews:"http://tour-pedia.org/api/getReviewsByPlaceId?placeId=243966", subCategory:"Spiritual Center"]]
    }

}
