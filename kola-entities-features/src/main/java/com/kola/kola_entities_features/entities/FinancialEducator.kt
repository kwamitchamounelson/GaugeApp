package com.kola.kola_entities_features.entities

import java.io.Serializable

data class FinancialEducator(
    var name: String,
    var email: String,
    var phone: String,
    val imageUrl:String,
    var moreInfo: String
):Serializable {
    constructor() : this("", "", "", "","")
}