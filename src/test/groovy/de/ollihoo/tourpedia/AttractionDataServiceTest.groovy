package de.ollihoo.tourpedia

import de.ollihoo.domain.Address
import de.ollihoo.domain.City
import de.ollihoo.domain.PointOfInterest
import de.ollihoo.osm.OsmService
import de.ollihoo.services.AddressService
import de.ollihoo.services.CityService
import de.ollihoo.services.PointOfInterestService

class AttractionDataServiceTest extends AttractionDataServiceTestBase {
  private static TOURPEDIA_JSON_RESPONSE =
      [TOURPEDIA_ENTRY_WITHOUT_ADDRESS, TOURPEDIA_ENTRY_WITH_ADDRESS,
       TOURPEDIA_ENTRY_WITH_CITY_COUNTRY_IN_ADDRESS, TOURPEDIA_ENTRY_WITH_NETHERLANDS_AS_ADDRESS]
  private static
  final Address VAN_KINSBERGENSTRAAT_6 = new Address(street: "Van Kinsbergenstraat 6", location: AttractionDataServiceTestBase.AMSTERDAM)

  private TourpediaService tourpediaService
  private CityService cityService
  private AddressService addressService
  private PointOfInterestService pointOfInterestService
  private AttractionDataService service
  private OsmService osmService

  def setup() {
    tourpediaService = Mock(TourpediaService)
    cityService = Mock(CityService)
    addressService = Mock(AddressService)
    pointOfInterestService = Mock(PointOfInterestService)
    osmService = Mock(OsmService)

    service = new AttractionDataService(
        tourpediaService: tourpediaService,
        cityService: cityService,
        addressService: addressService,
        pointOfInterestService: pointOfInterestService,
        osmService: osmService
    )
  }

  def "When city name is unkown, get osm information for city name" () {
    cityService.getCity("Amsterdam") >> null
    cityService.createOrUpdateCity(_) >> AMSTERDAM

    when:
    service.attractionsForAmsterdam

    then:
    1 * osmService.getPointOfInterestByRequest("Amsterdam") >> createCityAsPointOFInterest()
  }

  def "When city name is unkown, get lat, lng for city name" () {
    cityService.getCity("Amsterdam") >> null
    PointOfInterest amsterdam = createCityAsPointOFInterest()
    osmService.getPointOfInterestByRequest(_) >> amsterdam
    City actualCity

    when:
    service.attractionsForAmsterdam

    then:
    1 * cityService.createOrUpdateCity(_) >> { parameters -> actualCity = parameters[0]}
    actualCity.name == amsterdam.name
    actualCity.lat == amsterdam.lat
    actualCity.lng == amsterdam.lng
  }

  private PointOfInterest createCityAsPointOFInterest() {
    new PointOfInterest(name: "Amsterdam",
        lat: new BigDecimal("23.555"), lng: new BigDecimal("12"))
  }


  def "Get point of interests from tourpediaService"() {
    cityService.getCity("Amsterdam") >> AMSTERDAM

    when:
    service.attractionsForAmsterdam

    then:
    1 * tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> TOURPEDIA_JSON_RESPONSE
  }

  def "Get as instance of Amsterdam" () {
    when:
    service.attractionsForAmsterdam

    then:
    1 * cityService.getCity("Amsterdam") >> AMSTERDAM

  }

  def "Method parses all given elements"() {
    cityService.getCity("Amsterdam") >> AMSTERDAM
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> TOURPEDIA_JSON_RESPONSE
    when:
    def response = service.attractionsForAmsterdam

    then:
    response.size() == TOURPEDIA_JSON_RESPONSE.size()
  }

  def "When POI is new, it is saved" () {
    cityService.getCity("Amsterdam") >> AMSTERDAM
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]

    when:
    service.attractionsForAmsterdam

    then:
    1 * pointOfInterestService.insertOrUpdatePoi(_)
  }

  def "When address is null, add Amsterdam as location"() {
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]
    pointOfInterestService.insertOrUpdatePoi(_) >> { parameters -> parameters[0] }
    cityService.getCity("Amsterdam") >> AMSTERDAM

    when:
    def actualPointOfInterests = service.attractionsForAmsterdam

    then:
    actualPointOfInterests[0].location == AMSTERDAM
  }

  def "When address is set, it is used as location"() {
    cityService.getCity("Amsterdam") >> AMSTERDAM
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITH_NETHERLANDS_AS_ADDRESS]
    addressService.getOrCreateAddress(_, _) >> VAN_KINSBERGENSTRAAT_6
    pointOfInterestService.insertOrUpdatePoi(_) >> {parameters -> parameters[0]}

    when:
    def actualPointOfInterests = service.attractionsForAmsterdam

    then:
    actualPointOfInterests[0].location == VAN_KINSBERGENSTRAAT_6
  }

  def "When POI is found, it saves the original ID from tourpedia"() {
    cityService.getCity("Amsterdam") >> AMSTERDAM
    tourpediaService.getJsonResponseFor("Amsterdam", "attraction") >> [TOURPEDIA_ENTRY_WITHOUT_ADDRESS]
    pointOfInterestService.insertOrUpdatePoi(_)  >> { parameters -> parameters[0] }

    when:
    def actualPointOfInterests = service.attractionsForAmsterdam

    then:

    actualPointOfInterests[0].referenceId == "tourpedia#" + TOURPEDIA_ENTRY_WITHOUT_ADDRESS.id
  }


}
