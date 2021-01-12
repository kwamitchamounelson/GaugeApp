package com.example.gaugeapp.entities.communityLoan

import java.io.Serializable
import java.util.*

/**
 * Contribution community loan loan
 *
 * @property id automatically generated at the customer
 * @property contributorFullName, imageUrl are used to save readings
 * @constructor Create empty Contribution com loan
 */

data class ContributionComLoan(
    var id: String, var campaignId: String = "",
    var contributorId: String = "",
    var contributorFullName: String = "",
    var contributorImageUrl: String = "",
    var contributorScore: Double = 0.0,
    var amount: Double = 0.0,
    var contributionDate: Date = Date()
) : Serializable {


    constructor() : this("")

    //constructor() : this(UUID.randomUUID().toString())

}