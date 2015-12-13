package de.ollihoo.repository

import de.ollihoo.domain.Address
import org.springframework.data.neo4j.repository.GraphRepository

public interface AddressRepository extends GraphRepository<Address> {

    Address findByZipAndStreet(String zip, String street);
}
