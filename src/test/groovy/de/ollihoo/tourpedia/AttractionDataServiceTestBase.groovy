package de.ollihoo.tourpedia

import de.ollihoo.domain.Address
import de.ollihoo.domain.AdministrativeUnit
import de.ollihoo.domain.City
import spock.lang.Specification


class AttractionDataServiceTestBase extends Specification {
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


  static boolean isAmsterdamWithStreet(AdministrativeUnit location, String expectedStreet) {
    location instanceof Address && location.street == expectedStreet && isCityOfAmsterdam(location.location)
  }

  static boolean isCityOfAmsterdam(AdministrativeUnit location) {
    location == AMSTERDAM
  }

}
