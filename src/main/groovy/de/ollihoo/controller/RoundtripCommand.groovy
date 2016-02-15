package de.ollihoo.controller


class RoundtripCommand {
    String lat
    String lng

    BigDecimal getLatitude() {
        new BigDecimal(lat)
    }

    BigDecimal getLongitude() {
        new BigDecimal(lng)
    }
}
