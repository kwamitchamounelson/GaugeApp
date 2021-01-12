package com.example.gaugeapp.entities

class Localization(
    var long: Long,
    var lat: Long,
    var alt: Long,
    var address: String
) {
    constructor() : this(long = 0, lat = 0, alt = 0, address = "")
}