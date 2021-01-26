package com.kola.kola_entities_features.entities

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class SavedStoreInfo(
    val image:String,
    var id: String,
    val name: String,
    val address: String,
    var mobileMoneyNumbers: ArrayList<NumberForOperator>,
    val gpsLong: Float?,
    val gpsLat: Float?,
    val contactPhone:String,
    val date:Date
):Serializable {
    val opend:Date = Date()
    val close:Date = Date()
    constructor() : this("","","", "", ArrayList(),0F, 0F,"", Date())
}