package com.kola.kola_entities_features.entities

class Localization(
    val longitude: Double,
    val latitude: Double,
    val altitude: Double,
    val addresse: String
) {
    constructor() : this(0.0, 0.0, 0.0, "")
}