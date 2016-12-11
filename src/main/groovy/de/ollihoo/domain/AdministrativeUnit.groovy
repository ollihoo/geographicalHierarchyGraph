package de.ollihoo.domain

import org.neo4j.ogm.annotation.Transient


class AdministrativeUnit {
  BigDecimal lat
  BigDecimal lng

  @Transient
  private Coordinate coordinate

  Coordinate getCoordinate() {
    if (!coordinate) {
      setCoordinate()
    }
    coordinate
  }

  private synchronized setCoordinate() {
    if (lat && lng) {
      coordinate = new Coordinate(latitude: lat, longitude: lng)
    } else {
      throw new InvalidCoordinateException("Invalid coordinate: $lat/$lng")
    }
  }

}
