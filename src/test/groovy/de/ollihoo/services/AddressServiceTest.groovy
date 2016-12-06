package de.ollihoo.services

import de.ollihoo.domain.Address
import de.ollihoo.domain.AdministrativeUnit
import de.ollihoo.domain.City
import de.ollihoo.repository.AddressRepository
import spock.lang.Specification

class AddressServiceTest extends Specification {
  static City AMSTERDAM = new City(name: "Amsterdam")

  static TOURPEDIA_ENTRY_WITH_ADDRESS =
      [address    : "Van Kinsbergenstraat 6", category: "attraction",
       details    : "http://tour-pedia.org/api/getPlaceDetails?id=33989",
       id         : 33989, lat: 52.368381396984, lng: 4.8630453502768, location: "Amsterdam",
       name       : "SGI Amsterdam Cultural Centre",
       numReviews : 3, originalId: "4e8c99878b81e678900e7744", polarity: 7,
       reviews    : "http://tour-pedia.org/api/getReviewsByPlaceId?placeId=33989",
       subCategory: "Spiritual Center"]

  static TOURPEDIA_ENTRY_WITHOUT_ADDRESS =
      [address    : null, category: "attraction", details: "http://tour-pedia.org/api/getPlaceDetails?id=33985",
       id         : 33985, lat: 52.377262574894, lng: 4.8580104817941,
       location   : "Amsterdam", name: "Thenextweb",
       numReviews : 1, originalId: "5178d2aee4b0d969ecd65d3f", polarity: 5,
       reviews    : "http://tour-pedia.org/api/getReviewsByPlaceId?placeId=33985",
       subCategory: "Auditorium"]

  static TOURPEDIA_ENTRY_WITH_CITY_COUNTRY_IN_ADDRESS =
      [address    : "Eva Besnyöstraat 289, Amsterdam, Netherlands", category: "attraction",
       details    : "http://tour-pedia.org/api/getPlaceDetails?id=243966",
       id         : 243966, lat: 52.351137, lng: 5.005955, location: "Amsterdam", name: "freelance dee jay",
       numReviews : 3, originalId: "4e8c99878b81900e7744", polarity: 3,
       reviews    : "http://tour-pedia.org/api/getReviewsByPlaceId?placeId=243966",
       subCategory: "Spiritual Center"]

  static TOURPEDIA_ENTRY_WITH_NETHERLANDS_AS_ADDRESS =
      [address    : "Netherlands", category: "attraction",
       details    : "http://tour-pedia.org/api/getPlaceDetails?id=243966",
       id         : 243966, lat: 52.351137, lng: 5.005955, location: "Amsterdam", name: "freelance dee jay",
       numReviews : 3, originalId: "4e8c99878b81900e7744", polarity: 3,
       reviews    : "http://tour-pedia.org/api/getReviewsByPlaceId?placeId=243966",
       subCategory: "Spiritual Center"]
  private static
  final Address VAN_KINSBERGENSTRAAT_6 = new Address(street: "Van Kinsbergenstraat 6", location: AMSTERDAM)

  private AddressRepository addressRepository
  private AddressService service

  def setup() {
    addressRepository = Mock(AddressRepository)
    service = new AddressService(addressRepository: addressRepository)
  }

  def "When no street is given, response is null"() {
    when:
    def response = service.getOrCreateAddress(AMSTERDAM, TOURPEDIA_ENTRY_WITHOUT_ADDRESS)

    then:
    response == null
  }

  def "When street is given, response has address as location"() {
    addressRepository.save(_, 1) >> { parameters -> parameters[0] }

    when:
    Address response = service.getOrCreateAddress(AMSTERDAM, TOURPEDIA_ENTRY_WITH_ADDRESS)

    then:
    isAmsterdamWithStreet(response, "Van Kinsbergenstraat 6")
  }

  def "When street is given, a response address is tested to be in database"() {
    when:
    service.getOrCreateAddress(AMSTERDAM, TOURPEDIA_ENTRY_WITH_ADDRESS)

    then:
    1 * addressRepository.findByStreetAndLocation("Van Kinsbergenstraat 6", AMSTERDAM)
  }

  def "When street is found in database, entry is returned without saving"() {
    addressRepository.findByStreetAndLocation("Van Kinsbergenstraat 6", AMSTERDAM) >> VAN_KINSBERGENSTRAAT_6

    when:
    def response = service.getOrCreateAddress(AMSTERDAM, TOURPEDIA_ENTRY_WITH_ADDRESS)

    then:
    0 * addressRepository.save(_, _)
    response == VAN_KINSBERGENSTRAAT_6
  }

  def "When street is not found in database, entry is saved and returned"() {
    addressRepository.findByStreetAndLocation("Van Kinsbergenstraat 6", AMSTERDAM) >> null
    def savedAddress = null

    when:
    service.getOrCreateAddress(AMSTERDAM, TOURPEDIA_ENTRY_WITH_ADDRESS)

    then:
    1 * addressRepository.save(_, 1) >> { parameters ->
      savedAddress = (parameters[0].street == "Van Kinsbergenstraat 6") ? VAN_KINSBERGENSTRAAT_6 : null
      savedAddress
    }
    savedAddress == VAN_KINSBERGENSTRAAT_6
  }

  def "When 'Amsterdam, Netherlands' is in street name, remove it"() {
    addressRepository.save(_, 1) >> { parameters -> parameters[0] }

    when:
    Address response = service.getOrCreateAddress(AMSTERDAM, TOURPEDIA_ENTRY_WITH_CITY_COUNTRY_IN_ADDRESS)

    then:
    response.street == "Eva Besnyöstraat 289"
  }

  def "When 'Netherlands' is street name, return null"() {
    addressRepository.save(_, 1) >> { parameters -> parameters[0] }

    when:
    Address address = service.getOrCreateAddress(AMSTERDAM, TOURPEDIA_ENTRY_WITH_NETHERLANDS_AS_ADDRESS)

    then:
    address == null
  }

  static boolean isAmsterdamWithStreet(AdministrativeUnit location, String expectedStreet) {
    location instanceof Address && location.street == expectedStreet && location.location.name == "Amsterdam"
  }


}
