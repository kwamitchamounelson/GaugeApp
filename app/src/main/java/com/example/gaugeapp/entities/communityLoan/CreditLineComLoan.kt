package com.example.gaugeapp.entities.communityLoan

import java.io.Serializable
import java.util.*


/**
 * Credit line community loan loan
 *
 * NB: Very important:
 * 1- A line of credit can be made up of several campaigns, but only one active at a time.
 * 2- All campaigns have the same maximum repayment date that matches the repayment date of the line of credit
 * 3- The user has only one line of credit at any given time
 * 4- When a line of credit is solved, another will be created when the user creates a new campaign.
 *
 * @property id
 * @constructor Create empty Credit line com loan
 */

data class CreditLineComLoan(
    var id: String,

    /**
     * Repayment Date
     * End date of the line of credit, corresponds to the date on which the user must repay all the campaigns of this line of credit
     */
    var repaymentDate: Date = Date(),


    /**
     * Create date
     * Date of initiation of the credit line
     */

    var createDate: Date = Date(),
    var ownerId: String = "",


    /**
     * Solved
     * True if the user has reimbursed all the campaigns of this line
     */
    var solved: Boolean = false

) : Serializable {
    constructor() : this("")

}