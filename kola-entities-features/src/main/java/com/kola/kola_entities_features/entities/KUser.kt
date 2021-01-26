package com.kola.kola_entities_features.entities

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class KUser(
    var userUid: String,
    var userName: String,
    var fullName: String,
    var email: String,
    var imageUrL: String,
    var createdDate: Date,
    var authPhoneNumber: String,
    var mobileMoneyNumbers: ArrayList<NumberForOperator>
):Serializable {

    var currentAppVersionName=""
    var phoneNumbers= listOf<String>()
    val detectedName =""

    var budgets = listOf<String>()

    var city =""
    val bookmarkedListPersonalFinance = mutableListOf<String>()// containt all personnal finance that user already bookmark

    val userPersonalInfo:UserPersonalInfo = UserPersonalInfo()

    constructor() : this("", "", "", "", "", Date(), "", ArrayList())
    constructor(id: String) : this(id, "", "", "", "", Date(), "", ArrayList())
}