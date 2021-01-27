package com.example.gaugeapp.entities

import com.google.firebase.firestore.ServerTimestamp
import com.kola.kola_entities_features.entities.Localization
import java.util.*

data class Store(
    var uid: String,
    var name: String,
    var imageUrl: String,
    var omPhoneNumber: String,
    val mtnPhoneNumber: String,
    val address: String,
    val localization: Localization,
    @ServerTimestamp
    val createAt: Date? = null,
    val syncDate: Date
) {
    var description =""
    constructor() : this(
        uid = "",
        name = "",
        imageUrl = "",
        omPhoneNumber = "",
        mtnPhoneNumber = "",
        address = "",
        localization = Localization(),
        createAt = Date(),
        syncDate = Date()
    )
}