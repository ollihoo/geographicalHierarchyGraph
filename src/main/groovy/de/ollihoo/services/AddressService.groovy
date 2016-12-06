package de.ollihoo.services

import de.ollihoo.domain.Address
import de.ollihoo.domain.City
import de.ollihoo.repository.AddressRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AddressService {

  @Autowired
  private AddressRepository addressRepository

  Address getOrCreateAddress(City city, entry) {
    String trimmedAddress = removeUnexpectedEntriesFromStreet(entry.address)
    if (trimmedAddress) {
      Address existingAddress = addressRepository.findByStreetAndLocation(trimmedAddress, city)
      return existingAddress ?:
          addressRepository.save(new Address(street: trimmedAddress, zip: "", location: city), 1)
    }
    null
  }

  private removeUnexpectedEntriesFromStreet(String input) {
    if (input) {
      return input.replaceAll(", Amsterdam", "").replaceAll(/,? *Netherlands/, "")
    }
    null
  }


}
