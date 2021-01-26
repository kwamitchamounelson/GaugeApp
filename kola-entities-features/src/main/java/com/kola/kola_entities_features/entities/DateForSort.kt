package com.kola.kola_entities_features.entities

data class DateForSort (val year: Int, val monthOfYear: Int, val dayOfMont: Int, val dayOfWeek: String) {

    constructor():this(0,0,0, "")
}