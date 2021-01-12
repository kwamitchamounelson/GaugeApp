package com.example.gaugeapp.entities.communityLoan

import java.io.Serializable
import java.util.*

/**
 * Comment community loan
 *
 * @property id
 * @constructor Create empty Comment com loan
 */

data class CommentComLoan(
    var id: String, var campaignId: String = "",
    var text: String = "",
    var createAt: Date = Date(),
    var parentId: String = ""
) : Serializable {

    constructor() : this("")

}