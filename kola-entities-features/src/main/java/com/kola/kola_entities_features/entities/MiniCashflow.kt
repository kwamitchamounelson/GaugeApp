package com.kola.kola_entities_features.entities

import java.io.Serializable
import java.util.*

data class MiniCashflow (val id : String, val date: Date, val amount : Double, val category : String, val future: Boolean = false) : Serializable {
    constructor() : this("", Date(), 0.0, "")
}