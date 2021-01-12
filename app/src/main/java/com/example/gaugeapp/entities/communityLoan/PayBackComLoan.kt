package com.example.gaugeapp.entities.communityLoan

import java.io.Serializable
import java.util.*

/**
 * Pay back com loan
 *
 * @property contributionId represents the ID of the contribution to be reimbursed
 * @constructor Create empty Pay back com loan
 */

data class PayBackComLoan(
    var contributionId: String,

    /**
     * Owner id
     * represents a repayment, which can be either the originator of the loan or a guarantor
     */
    var ownerId: String = "",

    var campaignId: String = "",
    var amount: Double = 0.0,
    var payAt: Date = Date(),


    /**
     * Contributor id
     * The one to whom the user reimburses
     */
    var contributorId: String = ""

) : Serializable {

    constructor() : this("")
}