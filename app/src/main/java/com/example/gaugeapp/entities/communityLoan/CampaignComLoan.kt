package com.example.gaugeapp.entities.communityLoan

import java.io.Serializable
import java.util.*

/**
 * Campaign community loan
 *
 * NB: Very important:
 * 1- A campaign belongs to a line of credit only, and is made up of several contributions
 * 2- A campaign is automatically closed after 7 days after its creation
 *
 * @property id
 * @constructor Create empty Campaign com loan
 */



data class CampaignComLoan(
    var id: String,

    /**
     * Credit line id
     * The line of credit to which the campaign belongs
     */

    var creditLineId: String = "",


    /**
     * Owner id
     * The creator of the campaign, the one who makes the loan
     */
    var ownerId: String = "",

    var description: String = "",

    var payBackDate: Date = Date(),

    var amount: Double = 0.0,

    var reason: String = "",


    /**
     * Percent interest
     */

    var percentInterest: Double = 0.0,



    /**
     * Contributions list
     * List of campaign contributions
     * NB: We have used this property to avoid reading, by directly storing the information we need on site like the user's profile picture
     */

    var contributionsList: List<ContributionComLoan> = listOf(),


    /**
     * Contributor id list
     * List of campaign contributors' id
     * NB: We created this property because it is difficult for us to request the ContributorsList property in order to retrieve the campaigns for which the user is Contributor.
     */

    var contributorIdList: List<String> = listOf(),

    /**
     * Guarantors list
     * List of people who vouch for the campaign
     * NB: We have used this property to avoid reading, by directly storing the information we need on site like the user's profile picture
     */
    var guarantorsList: List<GuarantorComLoan> = listOf(),


    /**
     * Guarantor id list
     * This property allows you to list the campaigns so the user is Guarantor
     * NB: We created this property because it is difficult for us to request the guarantorsList property in order to retrieve the campaigns for which the user is Guarantor.
     */

    var guarantorIdList: List<String> = listOf(),


    /**
     * Pay back list
     */

    var payBackList: List<PayBackComLoan> = listOf(),

    /**
     * Create at
     * Date of campaign creation
     */

    var createAt: Date = Date(),


    /**
     * Syn date
     * to implement synchronisation
     */

    var synDate: Date = Date(),


    /**
     * Enable
     * If campaign is close or not
     */
    var enable: Boolean = false,


    /**
     * Solved
     * allows you to know if all the campaign loans have been paid
     */
    var solved: Boolean = false,

    var commentCount: Int = 0
) : Serializable {

    constructor() : this("")

}