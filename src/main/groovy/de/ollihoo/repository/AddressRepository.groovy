package de.ollihoo.repository

import de.ollihoo.domain.Address
import de.ollihoo.domain.AdministrativeUnit
import org.springframework.data.neo4j.repository.GraphRepository

public interface AddressRepository extends GraphRepository<Address> {

    Address findByStreetAndLocation(String street, AdministrativeUnit location)

    Address findByZipAndStreet(String zip, String street)
}
