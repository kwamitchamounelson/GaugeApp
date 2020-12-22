package com.example.gaugeapp.entities

import java.io.Serializable


/**
 * Guarantor community loan loan
 *
 * @property id automatically generated at the customer
 * @property userFullName, imageUrl are used to save readings
 * @constructor Create empty Guarantor com loan
 */

data class GuarantorComLoan(
    var userId: String, var userFullName: String = "",
    var campaignId: String = "",
    var imageUrl: String = "",
    var score: Double = 0.0
) : Serializable {
    constructor() : this("")

}