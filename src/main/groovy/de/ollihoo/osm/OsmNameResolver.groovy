package de.ollihoo.osm

import de.ollihoo.domain.Address
import de.ollihoo.domain.AdministrativeUnit
import de.ollihoo.domain.City
import de.ollihoo.domain.District
import de.ollihoo.domain.Sector
import de.ollihoo.repository.AddressRepository
import de.ollihoo.repository.CityRepository
import de.ollihoo.repository.DistrictRepository
import de.ollihoo.repository.SectorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OsmNameResolver {

    @Autowired
    AddressRepository addressRepository
    @Autowired
    CityRepository cityRepository
    @Autowired
    SectorRepository sectorRepository
    @Autowired
    DistrictRepository districtRepository

    String parsePoiName(String osmCombinedName) {
        splitCombinedName(osmCombinedName)[0]
    }

    AdministrativeUnit parseCombinedAddress(String osmCombinedName) {
        def addr = splitCombinedName(osmCombinedName)
        switch (addr.length) {
            case 9:
                def city = findOrCreateCity(addr[6])
                def sector = findOrCreateSector(addr[5], city)
                def district = findOrCreateDistrict(addr[4], sector)
                def address = findOrCreateAddress(district, addr[7], addr[2])
                return address
            case 8:
                def city = findOrCreateCity(addr[5])
                def sector = findOrCreateSector(addr[4], city)
                def district = findOrCreateDistrict(addr[3], sector)
                def address = findOrCreateAddress(district, addr[6], addr[2])
                return address

            case 7:
                def city = findOrCreateCity(addr[4])
                def sector = findOrCreateSector(addr[3], city)
                def district = findOrCreateDistrict(addr[2], sector)
                return findOrCreateAddress(district, addr[5], addr[1])

            case 6:
                def city = findOrCreateCity(addr[3])
                def sector = findOrCreateSector(addr[2], city)
                return findOrCreateAddress(sector, addr[4], addr[1])

            case 5:
                def city = findOrCreateCity(addr[2])
                return findOrCreateSector(addr[1], city)

            case 4:
                def city = findOrCreateCity(addr[2])
                return findOrCreateSector(addr[1], city)

            case 3:
                return findOrCreateCity(addr[1])
        }
        return null
    }

    private City findOrCreateCity(String cityName) {
        def city = cityRepository.findByName(cityName)?: new City(name: cityName)
        cityRepository.save(city, 1)
    }

    private Sector findOrCreateSector(String name, City city) {
        def sector = sectorRepository.findByName(name)?:new Sector(name: name, city: city)
        sectorRepository.save(sector, 1)
    }

    private District findOrCreateDistrict(String name, Sector sector) {
        def district = districtRepository.findByName(name)?: new District(name: name, sector: sector)
        districtRepository.save(district, 1)
    }

    private Address findOrCreateAddress(AdministrativeUnit parentLocation, String zip, String street) {
        def address = addressRepository.findByZipAndStreet(zip, street)?:
                new Address(
                street: street,
                zip: zip,
                location: parentLocation
        )
        addressRepository.save(address, 1)

    }

    private splitCombinedName(String input) {
        input.split("\t")[0].replaceAll(" ?, ?", ",").split(",")
    }

}
