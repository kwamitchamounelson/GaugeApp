package com.kola.kola_entities_features.entities

/**
 * Note
 * used for rating on the application
 * @property userId //user who make a note
 * @constructor
 *
 * @param note
 * @author Tsafack Dagha C. (Tsafix)

 */
class Note(val userId: String, note: Double) {
    constructor() : this("", 0.0)
}