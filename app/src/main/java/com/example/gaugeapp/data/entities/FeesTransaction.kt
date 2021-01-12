package com.example.gaugeapp.data.entities

import com.kola.smsmodule.enums.ENUM_SERVICE_ANALYSIS


data class FeesTransaction(
    var operateur: ENUM_SERVICE_ANALYSIS?,
    var name: String,
    var min: Double,
    var max: Double,
    var percentage: Double,
    var feesValues: Double
) {
    constructor() : this(null, "", 0.0, 0.0, 0.0, 0.0)
}