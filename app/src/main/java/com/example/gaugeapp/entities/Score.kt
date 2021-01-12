package com.example.gaugeapp.entities

import java.io.Serializable

/**
 * Score
 *
 * @property userId
 * @constructor Create empty Score
 */
data class Score(var userId: String) : Serializable {
    /**
     * Payback speed score of the user
     */
    var paybackSpeed: Double = 0.0

    /**
     * Transactions score of the user
     */
    var transactionsScore: Double = 0.0

    /**
     * Guaranteeing score of the user
     */
    var guaranteeingScore: Double = 0.0

    /**
     * Guarantors score of the user
     */
    var guarantorsScore: Double = 0.0

    constructor() : this("")
}