package com.kola.kola_entities_features.entities

/**
 * Country
 * to represent the country on application
 * @property name
 * @property IOSCode
 * @property currency
 * @constructor Create empty Country
 * @author Tsafack Dagha C. (Tsafix)
 */
data class Country(
    var name: String,
    var IOSCode: String,
    var currency: Currency
) {
    constructor() : this("", "", Currency())
}