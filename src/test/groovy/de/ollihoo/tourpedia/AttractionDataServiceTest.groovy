package de.ollihoo.tourpedia

import de.ollihoo.domain.Address
import de.ollihoo.domain.AdministrativeUnit
import de.ollihoo.domain.City
import de.ollihoo.repository.AddressRepository
import de.ollihoo.repository.CityRepository
import spock.lang.Specification

class AttractionDataServiceTest extends Specification {
    private static final City AMSTERDAM = new City(name: "Amsterdam")
    private static
    final Address VAN_KINSBERGENSTRAAT_6 = new Address(street: "Van Kinsbergenstraat 6", location: AMSTERDAM)

    private TourpediaService tourpediaService
    private CityRepository cityRepository
    private AddressRepository addressRepository
    private AttractionDataService service

    def setup() {
        tourpediaService = Mock(TourpediaService)
        cityRepository = Mock(CityRepository)
        addressRepository = Mock(AddressRepository)
        service = new AttractionDataService(
                tourpediaService: tourpediaService,
                cityRepository: cityRepository,
                addressRepository: addressRepository
        )
        cityRepository.save(_, _) >> AMSTERDAM
    }

    def "When Amsterdam is unknown, save it to the database"() {
        def usedCity = null
        when:
        service.attractionsForAmsterdam

        then:
        1 * cityRepository.findByName("Amsterdam") >> null
        1 * cityRepository.save(_, 1) >> { parameters ->
            usedCity = parameters[0].name == "Amsterdam" ? AMSTERDAM : null
        }
        usedCity == AMSTERDAM
    }

    def "When Amsterdam is known, return it without saving"() {
        when:
        service.attractionsForAmsterdam

        then:
        1 * cityRepository.findByName("Amsterdam") >> AMSTERDAM
        0 * cityRepository.save(_, _)
    }


    def "Get point of interests from tourpediaService"() {
        when:
        service.attractionsForAmsterdam

        then:
        1 * tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> TOURPEDIA_JSON_RESPONSE
    }

    def "Method parses all given elements"() {
        tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> TOURPEDIA_JSON_RESPONSE
        when:
        def response = service.attractionsForAmsterdam

        then:
        response.size() == TOURPEDIA_JSON_RESPONSE.size()
    }

    def "When no street is given, response has city as location"() {
        tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]
        when:
        def response = service.attractionsForAmsterdam

        then:
        isCityOfAmsterdam response[0].location
    }

    def "When street is given, response has address as location"() {
        tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_ADDRESS]
        addressRepository.save(_, 1) >> { parameters -> parameters[0] }

        when:
        def response = service.attractionsForAmsterdam

        then:
        isAmsterdamWithStreet(response[0].location, "Van Kinsbergenstraat 6")
    }

    def "When street is given, a response address is tested to be in database"() {
        tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_ADDRESS]
        when:
        service.attractionsForAmsterdam

        then:
        1 * addressRepository.findByStreetAndLocation("Van Kinsbergenstraat 6", AMSTERDAM)
    }

    def "When street is found in database, entry is returned without saving"() {
        tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_ADDRESS]
        addressRepository.findByStreetAndLocation("Van Kinsbergenstraat 6", AMSTERDAM) >> VAN_KINSBERGENSTRAAT_6

        when:
        def response = service.attractionsForAmsterdam

        then:
        response[0].location == VAN_KINSBERGENSTRAAT_6
    }

    def "When street is not found in database, entry is saved and returned"() {
        addressRepository.findByStreetAndLocation("Van Kinsbergenstraat 6", AMSTERDAM) >> null
        tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_ADDRESS]
        def savedAddress = null

        when:
        service.attractionsForAmsterdam

        then:
        1 * addressRepository.save(_, 1) >> { parameters ->
            savedAddress = (parameters[0].street == "Van Kinsbergenstraat 6") ? VAN_KINSBERGENSTRAAT_6 : null
            savedAddress
        }
        savedAddress == VAN_KINSBERGENSTRAAT_6
    }


    def "When POI is found, it saves the original ID from tourpedia"() {
        tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]

        when:
        def response = service.attractionsForAmsterdam

        then:
        response[0].referenceId == "tourpedia#33985"
    }

    def "When 'Amsterdam, Netherlands' is in street name, remove it"() {
        tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_CITY_COUNTRY_IN_ADDRESS]
        addressRepository.save(_, 1) >> { parameters -> parameters[0] }

        when:
        def responses = service.attractionsForAmsterdam

        then:
        responses[0].location.street == "Eva Besnyöstraat 289"
    }

    def "When 'Netherlands' is street name, remove it"() {
        tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_NETHERLANDS_AS_ADDRESS]
        addressRepository.save(_, 1) >> { parameters -> parameters[0] }

        when:
        def responses = service.attractionsForAmsterdam

        then:
        isCityOfAmsterdam(responses[0].location)
    }

    private boolean isAmsterdamWithStreet(AdministrativeUnit location, String expectedStreet) {
        location instanceof Address && location.street == expectedStreet && isCityOfAmsterdam(location.location)
    }

    private boolean isCityOfAmsterdam(AdministrativeUnit location) {
        location == AMSTERDAM
    }

    private static TOURPEDIA_ENTRY_WITH_ADDRESS =
            [address    : "Van Kinsbergenstraat 6", category: "attraction",
             details    : "http://tour-pedia.org/api/getPlaceDetails?id=33989",
             id         : 33989, lat: 52.368381396984, lng: 4.8630453502768, location: "Amsterdam",
             name       : "SGI Amsterdam Cultural Centre",
             numReviews : 3, originalId: "4e8c99878b81e678900e7744", polarity: 7,
             reviews    : "http://tour-pedia.org/api/getReviewsByPlaceId?placeId=33989",
             subCategory: "Spiritual Center"]

    private static TOURPEDIA_ENTRY_WITHOUT_ADDRESS =
            [address    : null, category: "attraction", details: "http://tour-pedia.org/api/getPlaceDetails?id=33985",
             id         : 33985, lat: 52.377262574894, lng: 4.8580104817941,
             location   : "Amsterdam", name: "Thenextweb",
             numReviews : 1, originalId: "5178d2aee4b0d969ecd65d3f", polarity: 5,
             reviews    : "http://tour-pedia.org/api/getReviewsByPlaceId?placeId=33985",
             subCategory: "Auditorium"]

    private static TOURPEDIA_ENTRY_WITH_CITY_COUNTRY_IN_ADDRESS =
            [address    : "Eva Besnyöstraat 289, Amsterdam, Netherlands", category: "attraction",
             details    : "http://tour-pedia.org/api/getPlaceDetails?id=243966",
             id         : 243966, lat: 52.351137, lng: 5.005955, location: "Amsterdam", name: "freelance dee jay",
             numReviews : 3, originalId: "4e8c99878b81900e7744", polarity: 3,
             reviews    : "http://tour-pedia.org/api/getReviewsByPlaceId?placeId=243966",
             subCategory: "Spiritual Center"]

    private static TOURPEDIA_ENTRY_WITH_NETHERLANDS_AS_ADDRESS =
            [address    : "Netherlands", category: "attraction",
             details    : "http://tour-pedia.org/api/getPlaceDetails?id=243966",
             id         : 243966, lat: 52.351137, lng: 5.005955, location: "Amsterdam", name: "freelance dee jay",
             numReviews : 3, originalId: "4e8c99878b81900e7744", polarity: 3,
             reviews    : "http://tour-pedia.org/api/getReviewsByPlaceId?placeId=243966",
             subCategory: "Spiritual Center"]

    private static TOURPEDIA_JSON_RESPONSE =
            [TOURPEDIA_ENTRY_WITHOUT_ADDRESS, TOURPEDIA_ENTRY_WITH_ADDRESS,
             TOURPEDIA_ENTRY_WITH_CITY_COUNTRY_IN_ADDRESS, TOURPEDIA_ENTRY_WITH_NETHERLANDS_AS_ADDRESS]

}
