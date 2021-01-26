package com.kola.kola_entities_features.entities

data class WrongTransNotifyer(var opName: String, var displayAlert: Boolean =true) {
    constructor():this("", true)
}