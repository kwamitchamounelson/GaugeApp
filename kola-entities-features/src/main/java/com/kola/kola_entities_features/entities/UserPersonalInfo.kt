package com.kola.kola_entities_features.entities

import java.util.*
import java.io.Serializable

/**
 * User personal info
 *
 * This is a property object that will be added to the user's properties.The user must have filled
 * in this object to have access to certain functionalities (forex agent in exchange, create a campaign
 * or stand surety in community loan)
 *
 * @property score
 * @property dateOfBirth
 * @property shortBio
 * @property jobType
 * @property educationLevel
 * @property gender
 * @property maritalStatus
 * @property nationalIdNumber
 * @property famillyInCharge
 * @property realImageUrl
 * @property cniFrontUrl
 * @property cniBackUrl
 * @property _isForexAgent if the current user is a forex agent
 * @property _isInternationalTransferAgent if the current user is an international transfer agent
 * @property forexNotes contains the note(s) of user(s)  who makes a rating on forex
 * @property chageNotes contains the note(s) of user(s) who makes a rating on notes
 * @property inTransferNotes contains the note(s) of user(s) who makes a rating on notes
 * @property forexRates contain currencies pair as String and conversionRate(per currencies pair) as Double for the user
 * @property inTransfertRates contain countries and conversionRate (per country) as Double for the user
 * @constructor Create empty User personal info
 *
 * @author Tsafack Dagha C. (Tsafix)
 */
data class UserPersonalInfo(
    val score: Score,
    var dateOfBirth: Date,
    var shortBio: String,
    var jobType: String,
    var educationLevel: String,
    var gender: String,
    var maritalStatus: String,
    var nationalIdNumber: String,
    var famillyInCharge: Boolean,
    var realImageUrl: String,
    var cniFrontUrl: String,
    var cniBackUrl: String,
    var _isForexAgent: Boolean,
    var _isInternationalTransferAgent: Boolean,
    var forexNotes: List<Note>?,
    var chageNotes: List<Note>?,
    val inTransferNotes: List<Note>?,
    var forexRates: MutableMap<String, Double>?,
    var inTransfertRates: Map<Country, Double>?
) : Serializable {
    constructor() : this(
        score = Score(),
        dateOfBirth = Date(),
        shortBio = "",
        jobType = "",
        educationLevel = "",
        gender = "",
        maritalStatus = "",
        nationalIdNumber = "",
        famillyInCharge = false,
        realImageUrl = "",
        cniFrontUrl = "",
        cniBackUrl = "",
        _isForexAgent = false,
        _isInternationalTransferAgent = false,
        forexNotes = listOf(),
        chageNotes = listOf(),
        inTransferNotes = listOf(),
        forexRates = mutableMapOf(),
        inTransfertRates = mutableMapOf()
    )
}