package com.kola.kola_entities_features.entities

import java.io.Serializable

data class NumberForOperator(
    val operatorName: String,
    var phoneNumber: String,
    var simCartIccId:String =""
) :Serializable{

    constructor() : this("", "")
}