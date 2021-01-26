package com.kola.kola_entities_features.entities


/**
 * Currency
 * to represent currency on application
 * @property name
 * @property symbol
 * @constructor Create empty Currency
 * @author Tsafack Dagha C. (Tsafix)
 */
data class Currency(
    val name: String,
    val symbol: String,
    val code: String,
    val flagResource: Int
) {
    constructor() : this("", "", "", -1)
}